#pragma once

#include <traits.hpp>
#include <asgl/gl_utils.hpp>
#include <asgl/gl_free.hpp>
#include <memory>

extern "C" {
#include "libavutil/frame.h"
}

namespace bilias::gl {

    class GLRenderer : NonCopy {
    protected:
        bool initialized{false};
        int viewport_width{};
        int viewport_height{};

    public:
        GLRenderer();
        virtual ~GLRenderer();

        virtual auto initialize() -> void = 0;
        virtual auto render() -> void = 0;
        virtual auto render_frame(AVFrame *frame) -> bool = 0;
        virtual auto destroy() -> void {}

        auto set_viewport(int width, int height) -> void;
        auto is_initialized() const noexcept -> bool { return initialized; }


    public:
        static auto create() -> std::unique_ptr<GLRenderer>;

    };
}