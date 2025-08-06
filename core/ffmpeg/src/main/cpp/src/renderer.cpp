#include "renderer.hpp"
#include <android/log.h>
#include <mutex>

#define LOG_TAG "VideoRenderer"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

namespace {
     constexpr char VERTEX_SHADER[] =
            "#version 300 es\n"
            "layout (location = 0) in vec4 vPosition;\n"
            "void main() {\n"
            "    gl_Position = vPosition;\n"
            "}\n";

    constexpr char FRAGMENT_SHADER[] =
            "#version 300 es\n"
            "precision mediump float;\n"
            "out vec4 fragColor;\n"
            "void main() {\n"
            "    fragColor = vec4(1.0, 0.41, 0.71, 1.0);\n"
            "}\n";

    auto read_packet(void* opaque, uint8_t* buf, int buf_size) -> int {
        int fd = (int)(intptr_t)opaque;
        return read(fd, buf, buf_size);
    }

    auto seek_packet(void* opaque, int64_t offset, int whence) -> int64_t {
        int fd = (int)(intptr_t)opaque;
        return lseek64(fd, offset, whence);
    }

}

namespace bilias {


    VideoRenderer::VideoRenderer() : program(0), position_handle(0),
    vbo(0), vao(0) {}

    VideoRenderer::~VideoRenderer() {
        should_stop = true;
        if (decode_thread.joinable()) {
            decode_thread.join();
        }
        cleanup_ffmpeg();
        cleanup_opengl();
    }

    auto VideoRenderer::create_shader(GLenum type, std::string_view source) -> GLuint {
        auto shader = glCreateShader(type);
        auto *source_ptr = source.data();
        if (shader) [[likely]] {
            glShaderSource(shader, 1, &source_ptr, nullptr);
            glCompileShader(shader);
            GLint compiled = 0;
            glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
            if (!compiled) {
                GLint infoLen = 0;
                glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
                if (infoLen) {
                    char* buf = new char[infoLen];
                    glGetShaderInfoLog(shader, infoLen, nullptr, buf);
                    LOGE("Could not compile shader %d:\n%s\n",
                         type, buf);
                    delete[] buf;
                }
                glDeleteShader(shader);
                shader = 0;
            }

        }
        return shader;
    }

    auto VideoRenderer::create_program(
            std::string_view vertex_source,
            std::string_view fragment_source
   ) -> GLuint {
        auto vertex_shader = this->create_shader(GL_VERTEX_SHADER, vertex_source);
        if (!vertex_shader) {
            return 0;
        }
        auto fragment_shader = this->create_shader(GL_FRAGMENT_SHADER, fragment_source);
        if (!fragment_shader) {
            glDeleteShader(vertex_shader);
            return 0;
        }
        auto p = glCreateProgram();
        if (p) {
            glAttachShader(p, vertex_shader);
            glAttachShader(p, fragment_shader);
            glLinkProgram(p);
            GLint linkStatus = GL_FALSE;
            glGetProgramiv(p, GL_LINK_STATUS, &linkStatus);
            if (linkStatus != GL_TRUE) {
                GLint bufLength = 0;
                glGetProgramiv(p, GL_INFO_LOG_LENGTH, &bufLength);
                if (bufLength) {
                    char* buf = new char[bufLength];
                    glGetProgramInfoLog(p, bufLength, NULL, buf);
                    LOGE("Could not link program:\n%s\n", buf);
                    delete[] buf;
                }
                glDeleteProgram(p);
                p = 0;
            }
        }
        glDeleteShader(vertex_shader);
        glDeleteShader(fragment_shader);
        return p;
    }

    auto VideoRenderer::on_surface_created() -> void {
        LOGI("native on_surface_created");
        program = create_program(VERTEX_SHADER, FRAGMENT_SHADER);
        if (!program) {
            LOGE("Failed to create program.");
            return;
        }
        float vertices[] = {
                -1.0f, -1.0f,  // 左下
                1.0f, -1.0f,  // 右下
                -1.0f,  1.0f,  // 左上
                1.0f,  1.0f   // 右上
        };

        glGenVertexArrays(1, &vao);
        glGenBuffers(1, &vbo);
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

        // position attribute
        glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), nullptr);
        glEnableVertexAttribArray(0);

        // texture coordinate attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 4 * sizeof(float), (void*)(2 * sizeof(float)));
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        texture_handle = glGetUniformLocation(program, "uTexture");
        setup_texture();

        if (video_fd > 0) {
            if (init_ffmpeg()) {
                start_playback();
            }
        }
        LOGI("native on_surface_created done");
    }

    auto VideoRenderer::on_surface_changed(int width, int height) -> void {
        glViewport(0, 0, width, height);
    }

    auto VideoRenderer::on_draw_frame() -> void {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(program);
        update_texture();

        // bind texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture_id);
        glUniform1i(texture_handle, 0);

        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glBindVertexArray(0);


    }

    auto VideoRenderer::cleanup_opengl() -> void {
        if (auto b = std::exchange(this->vbo, 0); b != 0) {
            glDeleteBuffers(1, &b);
        }
        if (auto a = std::exchange(this->vao, 0); a != 0) {
            glDeleteVertexArrays(1, &a);
        }
        if (auto p = std::exchange(this->program, 0); p != 0) {
            glDeleteProgram(p);
        }
    }

    auto VideoRenderer::init_ffmpeg() -> bool {
        if (video_fd < 0) {
            LOGE("Video path is invalid");
            return false;
        }

        // open video file
        const int buffer_size = 4096;
        uint8_t* buf = (uint8_t*)av_malloc(buffer_size);

        avio_ctx = avio_alloc_context(buf, 4096, 0, (void *)video_fd, read_packet, nullptr, seek_packet);
        if (!avio_ctx) {
            LOGE("Could not allocate AVIO context");
            av_free(buf);
            return false;
        }

        // 分配格式上下文
        format_ctx = avformat_alloc_context();
        if (!format_ctx) {
            LOGE("Could not allocate format context");
            return false;
        }

        // 设置自定义 IO
        format_ctx->pb = avio_ctx;

        if (avformat_open_input(&format_ctx, nullptr, nullptr, nullptr) < 0) {
            LOGE("Could not open input from file descriptor");
            return false;
        }

        // find video stream
        if (avformat_find_stream_info(format_ctx, nullptr) < 0) {
            LOGE("Could not find stream information");
            return false;
        }

        // find video stream
        video_stream_index = av_find_best_stream(format_ctx, AVMEDIA_TYPE_VIDEO, -1, -1, &codec, 0);
        if (video_stream_index < 0) {
            LOGE("Could not find video stream");
            return false;
        }

        // create video codec context
        codec_ctx = avcodec_alloc_context3(codec);
        if (!codec_ctx) {
            LOGE("Could not allocate video codec context");
            return false;
        }

        // copy codec parameters to video codec context
        if (avcodec_parameters_to_context(codec_ctx, format_ctx->streams[video_stream_index]->codecpar) < 0) {
            LOGE("Could not copy codec parameters to context");
            return false;
        }

        // open video codec
        if (avcodec_open2(codec_ctx, codec, nullptr) < 0) {
            LOGE("Could not open codec");
            return false;
        }

        video_width = codec_ctx->width;
        video_height = codec_ctx->height;

        // allocate video frame
        frame = av_frame_alloc();
        frame_rgb = av_frame_alloc();
        packet = av_packet_alloc();

        if (!frame || !frame_rgb || !packet) {
            LOGE("Could not allocate frame or packet");
            return false;
        }

        int num_bytes = av_image_get_buffer_size(AV_PIX_FMT_RGB24, video_width, video_height, 1);
        buffer = (uint8_t*)av_malloc(num_bytes * sizeof(uint8_t));

        av_image_fill_arrays(frame_rgb->data, frame_rgb->linesize, buffer, AV_PIX_FMT_RGB24, video_width, video_height, 1);

        sws_ctx = sws_getContext(video_width, video_height, codec_ctx->pix_fmt,
                                 video_width, video_height, AV_PIX_FMT_RGB24,
                                 SWS_BILINEAR, nullptr, nullptr, nullptr);


        if (!sws_ctx) {
            LOGE("Could not initialize the conversion context");
            return false;
        }

        LOGI("FFmpeg initialized successfully. Video: %dx%d", video_width, video_height);
        return true;


    }

    auto VideoRenderer::cleanup_ffmpeg() -> void {
        if (sws_ctx) {
            sws_freeContext(sws_ctx);
            sws_ctx = nullptr;
        }
        if (buffer) {
            av_free(buffer);
            buffer = nullptr;
        }
        if (frame) {
            av_frame_free(&frame);
        }
        if (frame_rgb) {
            av_frame_free(&frame_rgb);
        }
        if (packet) {
            av_packet_free(&packet);
        }
        if (codec_ctx) {
            avcodec_free_context(&codec_ctx);
        }
        if (format_ctx) {
            avformat_close_input(&format_ctx);
        }

    }

    auto VideoRenderer::decode_loop() -> void {
        while (!should_stop && is_playing) {
            if (av_read_frame(format_ctx, packet) >= 0) {
                if (packet->stream_index == video_stream_index) {
                    // send data to decoder
                    if (avcodec_send_packet(codec_ctx, packet) == 0) {

                        if (avcodec_receive_frame(codec_ctx, frame) == 0) {
                            // to RGB
                            sws_scale(sws_ctx, frame->data, frame->linesize, 0, video_height,
                                      frame_rgb->data, frame_rgb->linesize);

                            {
                                std::lock_guard<std::mutex> lock(frame_mutex);
                                has_new_frame = true;
                            }
                        }
                    }
                }
                av_packet_unref(packet);
            } else {
                // loop
                av_seek_frame(format_ctx, video_stream_index, 0, AVSEEK_FLAG_FRAME);
            }

            // speed
            std::this_thread::sleep_for(std::chrono::milliseconds(33)); // ~30fps
        }

    }

    auto VideoRenderer::setup_texture() -> void {
        glGenTextures(1, &texture_id);
        glBindTexture(GL_TEXTURE_2D, texture_id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glBindTexture(GL_TEXTURE_2D, 0);

    }

    auto VideoRenderer::update_texture() -> void {
        std::lock_guard<std::mutex> lock(frame_mutex);
        if (has_new_frame && frame_rgb && buffer) {
            glBindTexture(GL_TEXTURE_2D, texture_id);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, video_width, video_height, 0,
                         GL_RGB, GL_UNSIGNED_BYTE, buffer);
            glBindTexture(GL_TEXTURE_2D, 0);
            has_new_frame = false;
        }

    }

    auto VideoRenderer::start_playback() -> void {
        LOGI("native start_playback");
        if (!is_playing && video_fd != -1) {
            LOGI("native start_playback start");
            is_playing = true;
            should_stop = false;
            decode_thread = std::thread(&VideoRenderer::decode_loop, this);
        }
    }

    auto VideoRenderer::pause_playback() -> void {
        is_playing = false;
    }

    auto VideoRenderer::set_video_fd(int fd) -> void {
        LOGI("native set_video_fd %d", fd);
        video_fd = fd;
    }


}