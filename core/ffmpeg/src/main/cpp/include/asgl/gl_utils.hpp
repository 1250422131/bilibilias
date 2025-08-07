#pragma once

#include <android/native_window.h>
#include <EGL/egl.h>
#include <asgl/gl_free.hpp>
#include <GLES3/gl3.h>
#include <jni_log.hpp>
#include <string_view>
#include <span>

namespace bilias::gl {

    auto create_shader(GLenum type, std::string_view source) -> GLShaderPtr;

    auto create_program(std::string_view vertex_source, std::string_view fragment_source) -> GLProgramPtr;

    auto init_gl(ANativeWindow *window) -> void;

    auto create_vertex_buffer(std::span<const float> vertices) -> GLBufferPtr;

    auto create_texture(GLenum target) -> GLTexturePtr;

    auto setup_texture_params(GLuint texture, GLenum target) -> void;
}