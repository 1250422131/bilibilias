#include <asgl/gl_renderer.hpp>
#include <ffmpeg/ffmpeg_util.hpp>

namespace {
    constexpr std::string_view YUV_TO_RGB_VERTEX_SHADER =
            "attribute vec4 a_position;\n"
            "attribute vec2 a_tex_coord;\n"
            "varying vec2 v_tex_coord;\n"
            "void main() {\n"
            "    gl_Position = a_position;\n"
            "    v_tex_coord = a_tex_coord;\n"
            "}\n";

    constexpr std::string_view YUV_TO_RGB_FRAGMENT_SHADER =
            "precision mediump float;\n"
            "varying vec2 v_tex_coord;\n"
            "uniform sampler2D y_texture;\n"
            "uniform sampler2D u_texture;\n"
            "uniform sampler2D v_texture;\n"
            "void main() {\n"
            "    float y = texture2D(y_texture, v_tex_coord).r;\n"
            "    float u = texture2D(u_texture, v_tex_coord).r - 0.5;\n"
            "    float v = texture2D(v_texture, v_tex_coord).r - 0.5;\n"
            "    \n"
            "    float r = y + 1.402 * v;\n"
            "    float g = y - 0.344 * u - 0.714 * v;\n"
            "    float b = y + 1.772 * u;\n"
            "    \n"
            "    gl_FragColor = vec4(r, g, b, 1.0);\n"
            "}\n";
}

namespace bilias::gl {

    auto GLRenderer::set_viewport(int width, int height) -> void {
        viewport_width = width;
        viewport_height = height;
        if (initialized) {
            glViewport(0, 0, width, height);
        }
    }

    GLRenderer::~GLRenderer() = default;

    GLRenderer::GLRenderer() = default;

    namespace {
        class YUVRenderer final : public GLRenderer {
        private:
            GLProgramPtr program{};
            GLBufferPtr vertex_buffer{};

            GLTexturePtr texture_y{};
            GLTexturePtr texture_u{};
            GLTexturePtr texture_v{};

            struct {
                GLint position{};
                GLint tex_coord{};
                GLint y_sampler{};
                GLint u_sampler{};
                GLint v_sampler{};
            } locations{};

            AVFrame *current_frame{};
            int frame_width{};
            int frame_height{};

        public:
            YUVRenderer() : GLRenderer() {}

            ~YUVRenderer() override = default;

            auto initialize() -> void override {
                if (initialized) return;
                if (!create_shader_program()) {
                    LOGE("create shader program failed");
                    throw std::runtime_error("create shader program failed");
                }

                setup_vertex_data();
                setup_textures();
                initialized = true;
            }

            auto render() -> void override {
                if (!initialized || !current_frame) return;

                update_YUV_textures(current_frame);

                glUseProgram(program);
                glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);

                glEnableVertexAttribArray(locations.position);
                glEnableVertexAttribArray(locations.tex_coord);

                glVertexAttribPointer(locations.position, 2, GL_FLOAT, GL_FALSE,
                                      4 * sizeof(float), nullptr);
                glVertexAttribPointer(locations.tex_coord, 2, GL_FLOAT, GL_FALSE,
                                      4 * sizeof(float), reinterpret_cast<void*>(2 * sizeof(float)));

                glUniform1i(locations.y_sampler, 0);
                glUniform1i(locations.u_sampler, 1);
                glUniform1i(locations.v_sampler, 2);

                glViewport(0, 0, viewport_width, viewport_height);
                glClear(GL_COLOR_BUFFER_BIT);
                glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

                glDisableVertexAttribArray(locations.position);
                glDisableVertexAttribArray(locations.tex_coord);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
            }

            auto render_frame(AVFrame *frame) -> bool override {
                if (!frame) return false;

                update_frame(frame);
                render();
                return true;
            }

            auto destroy() -> void override {
                if (initialized) {
                    current_frame = nullptr;
                    program.reset();
                    vertex_buffer.reset();
                    texture_y.reset();
                    texture_u.reset();
                    texture_v.reset();
                    GLRenderer::destroy();
                }
            }


            auto update_frame(AVFrame *frame) -> void {
                current_frame = frame;
                frame_width = frame->width;
                frame_height = frame->height;
            }

            auto update_YUV_textures(AVFrame *frame) -> void {
                // Y plane
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, texture_y);
                update_texture_data(texture_y, frame->width, frame->height, frame->data[0], frame->linesize[0]);

                // U plane
                glActiveTexture(GL_TEXTURE1);
                glBindTexture(GL_TEXTURE_2D, texture_u);
                update_texture_data(texture_u, frame->width / 2, frame->height / 2, frame->data[1], frame->linesize[1]);

                // V plane
                glActiveTexture(GL_TEXTURE2);
                glBindTexture(GL_TEXTURE_2D, texture_v);
                update_texture_data(texture_v, frame->width / 2, frame->height / 2, frame->data[2], frame->linesize[2]);
            }

            auto update_texture_data(
                GLuint texture,
                int width,
                int height,
                uint8_t *data,
                int linesize
            ) -> void {
                if (linesize == width) {
                    // No padding, can upload directly
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, width, height, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, data);
                } else {
                    // Handle padding by uploading row by row
                    glPixelStorei(GL_UNPACK_ROW_LENGTH, linesize);
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, width, height, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, data);
                    glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
                }
            }

            auto setup_textures() -> void {
                texture_y = create_texture(GL_TEXTURE_2D);
                texture_u = create_texture(GL_TEXTURE_2D);
                texture_v = create_texture(GL_TEXTURE_2D);

                setup_texture_params(texture_y, GL_TEXTURE_2D);
                setup_texture_params(texture_u, GL_TEXTURE_2D);
                setup_texture_params(texture_v, GL_TEXTURE_2D);
            }

        private:
            auto create_shader_program() -> bool {
                program = create_program(YUV_TO_RGB_VERTEX_SHADER, YUV_TO_RGB_FRAGMENT_SHADER);
                if (!program) {
                    LOGE("create program failed");
                    return false;
                }
                locations.position = glGetAttribLocation(program, "a_position");
                locations.tex_coord = glGetAttribLocation(program, "a_tex_coord");
                locations.y_sampler = glGetUniformLocation(program, "y_texture");
                locations.u_sampler = glGetUniformLocation(program, "u_texture");
                locations.v_sampler = glGetUniformLocation(program, "v_texture");

                if (locations.position == -1 || locations.tex_coord == -1 ||
                    locations.y_sampler == -1 || locations.u_sampler == -1 || locations.v_sampler == -1) {
                    LOGE("Failed to get shader locations");
                    return false;
                }
                return true;
            }

            auto setup_vertex_data() -> void {
                // 顶点数据 (position + texture coordinates)
                constexpr float vertices[] = {
                        -1.0f, -1.0f, 0.0f, 1.0f,  // 左下角
                        1.0f, -1.0f, 1.0f, 1.0f,  // 右下角
                        -1.0f,  1.0f, 0.0f, 0.0f,  // 左上角
                        1.0f,  1.0f, 1.0f, 0.0f   // 右上角

                };
                vertex_buffer = create_vertex_buffer(vertices);
            }

        };
    }

    auto GLRenderer::create() -> std::unique_ptr<GLRenderer> {
        return std::unique_ptr<YUVRenderer>();
    }
}