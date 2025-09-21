#pragma once

#include <string_view>
#include <thread>
#include <GLES3/gl3.h>
#include <traits.hpp>
#include <asgl/egl_manager.hpp>
#include <asgl/gl_renderer.hpp>

extern "C" {
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
}


namespace bilias {

    class VideoRenderer final : NonCopy {
    private:
        std::unique_ptr<gl::EGLManager> egl_manager{};
        std::unique_ptr<gl::GLRenderer> renderer{};

        std::atomic<bool> initialized{false};
        std::mutex render_mutex{};
    public:
        VideoRenderer();
        ~VideoRenderer();

        auto initialize(ANativeWindow *native_window) -> bool;
        auto render_frame(AVFrame *frame) -> bool;
        auto set_viewport(int width, int height) -> void;
        auto destroy() -> void;

        // EGL context management for the render thread
        auto setup_context() -> bool;
        auto tear_down_context() -> void;
        auto swap_buffers() -> void;

        auto is_initialized() const noexcept -> bool { return initialized; }
    };
}
