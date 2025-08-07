#include <asgl/gl_utils.hpp>

namespace {
    EGLDisplay display;
    EGLSurface surface;
    EGLContext context;
    GLuint texture;

}

namespace bilias::gl {

    auto create_shader(GLenum type, std::string_view source) -> GLShaderPtr {
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

    auto create_program(std::string_view vertex_source, std::string_view fragment_source) -> GLProgramPtr {
        auto vertex_shader = create_shader(GL_VERTEX_SHADER, vertex_source);
        if (!vertex_shader.get()) {
            return 0;
        }
        auto fragment_shader = create_shader(GL_FRAGMENT_SHADER, fragment_source);
        if (!fragment_shader.get()) {
            return 0;
        }
        auto p = glCreateProgram();
        if (p) {
            glAttachShader(p, vertex_shader.get());
            glAttachShader(p, fragment_shader.get());
            glLinkProgram(p);
            GLint linkStatus = GL_FALSE;
            glGetProgramiv(p, GL_LINK_STATUS, &linkStatus);
            if (linkStatus != GL_TRUE) {
                GLint bufLength = 0;
                glGetProgramiv(p, GL_INFO_LOG_LENGTH, &bufLength);
                if (bufLength) {
                    char* buf = new char[bufLength];
                    glGetProgramInfoLog(p, bufLength, nullptr, buf);
                    LOGE("Could not link program:\n%s\n", buf);
                    delete[] buf;
                }
                glDeleteProgram(p);
                p = 0;
            }
        }
        return p;
    }


    auto init_gl(ANativeWindow *window) -> void {
        display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
        eglInitialize(display, nullptr, nullptr);
        constexpr EGLint configAttribs[] = {
                EGL_RENDERABLE_TYPE, EGL_OPENGL_ES3_BIT,
                EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
                EGL_BLUE_SIZE, 8, EGL_GREEN_SIZE, 8, EGL_RED_SIZE, 8,
                EGL_NONE
        };

        EGLConfig config;
        eglChooseConfig(display, configAttribs, &config, 1, nullptr);

        context = eglCreateContext(display, config, EGL_NO_CONTEXT, nullptr);
        surface = eglCreateWindowSurface(display, config, window, nullptr);
        eglMakeCurrent(display, surface, surface, context);

        glGenTextures(1, &texture);
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    auto create_vertex_buffer(std::span<const float> vertices) -> GLBufferPtr {
        GLuint buffer{};
        glGenBuffers(1, &buffer);
        glBindBuffer(GL_ARRAY_BUFFER, buffer);
        glBufferData(GL_ARRAY_BUFFER, static_cast<int>(vertices.size() * sizeof(float)), vertices.data(), GL_STATIC_DRAW);
        return buffer;
    }

    auto create_texture(GLenum target) -> GLTexturePtr {
        GLuint texture{};
        glGenTextures(1, &texture);
        return texture;
    }

    auto setup_texture_params(GLuint texture, GLenum target) -> void {
        glBindTexture(target, texture);
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    auto render_frame(uint8_t *rgb_data, int width, int height) -> void {
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, rgb_data);

        glClear(GL_COLOR_BUFFER_BIT);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        eglSwapBuffers(display, surface);
    }
}