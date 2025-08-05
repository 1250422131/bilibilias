#pragma once

#include <string_view>
#include <GLES3/gl3.h>

namespace bilias {

    class VideoRenderer {
        GLuint program;
        GLuint position_handle;
        GLuint vbo;
        GLuint vao;

    public:
        VideoRenderer();
        ~VideoRenderer();

        auto on_surface_created() -> void;
        auto on_surface_changed(int width, int height) -> void;
        auto on_draw_frame() -> void;

    private:
        auto create_shader(GLenum type, std::string_view  source) -> GLuint;
        auto create_program(std::string_view vertex_source, std::string_view fragment_source) -> GLuint;
    };
}