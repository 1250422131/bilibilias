#include <renderer.hpp>

#include <android/log.h>

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

}

namespace bilias {


    VideoRenderer::VideoRenderer() : program(0), position_handle(0),
    vbo(0), vao(0) {}

    VideoRenderer::~VideoRenderer() {
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

        glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), nullptr);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }

    auto VideoRenderer::on_surface_changed(int width, int height) -> void {
        glViewport(0, 0, width, height);
    }

    auto VideoRenderer::on_draw_frame() -> void {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(program);
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glBindVertexArray(0);

    }

}