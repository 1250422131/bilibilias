#pragma once

#include <traits.hpp>
#include <EGL/egl.h>
#include <GLES3/gl3.h>
#include <concepts>

namespace bilias::gl {

    inline auto free_program(GLuint program) -> void {
        glDeleteProgram(program);
    }

    inline auto free_shader(GLuint shader) -> void {
        glDeleteShader(shader);
    }

    inline auto free_buffers(GLuint buffer) -> void {
        glDeleteBuffers(1, &buffer);
    }

    inline auto free_texture(GLuint texture) -> void {
        glDeleteTextures(1, &texture);
    }

    template<auto Fn>
    class GLFree : public NonCopy {
    protected:
        GLuint value;

    public:
        constexpr GLFree(GLuint v) : value(v) {}
        constexpr GLFree() : value(0) {}

        GLFree(GLFree &&other) noexcept : value(std::exchange(other.value, 0)) {

        }

        GLFree &operator=(GLFree &&other) noexcept {
            if (this != &other) {
                if (this->value > 0) {
                    Fn(this->value);
                    this->value = std::exchange(other.value, 0);
                }
            }
            return *this;
        }


        operator GLuint() const noexcept {
            return get();
        }

        auto get() const noexcept -> GLuint {
            return value;
        }

        auto reset() noexcept -> void {
            auto v = std::exchange(this->value, 0);
            if (v > 0) {
                Fn(v);
            }
        }

        ~GLFree() {
            auto v = std::exchange(this->value, -1);
            if (v > 0) {
                Fn(v);
            }
        }
    };



    using GLProgramPtr = GLFree<free_program>;
    using GLShaderPtr = GLFree<free_shader>;
    using GLBufferPtr = GLFree<free_buffers>;
    using GLTexturePtr = GLFree<free_texture>;

}