#pragma once

#include <EGL/egl.h>
#include <EGL/eglext.h>
#include <android/native_window.h>
#include <traits.hpp>


namespace bilias::gl {

    class EGLManager final : NonCopy {
    private:
        EGLDisplay display{};
        EGLContext context{};
        EGLSurface surface{};
        EGLConfig config{};
        ANativeWindow *native_window{};
        bool initialized{false};

    public:
        EGLManager();
        ~EGLManager();

        [[nodiscard]]
        auto initialize(ANativeWindow *window) -> bool;

        auto make_current() -> void;

        auto swap_buffers() -> void;

        auto destroy() -> void;

        auto get_display() const noexcept -> EGLDisplay { return display; }
        auto get_context() const noexcept -> EGLContext { return context; }
        auto get_surface() const noexcept -> EGLSurface { return surface; }
        auto get_config() const noexcept -> EGLConfig { return config; }
        auto is_initialized() const noexcept -> bool { return initialized; }

    private:
        auto init_egl() -> bool;
        auto create_surface(ANativeWindow *window) -> bool;
        auto create_context() -> bool;
        auto cleanup_egl() -> void;
    };
}