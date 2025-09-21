#include "video_renderer.hpp"
#include <mutex>
#include <jni_log.hpp>

namespace bilias {

    VideoRenderer::VideoRenderer() = default;

    VideoRenderer::~VideoRenderer() {
        destroy();
    }

    auto VideoRenderer::initialize(ANativeWindow *native_window) -> bool {
        log_i("VideoRenderer::initialize window: %ld", (long) native_window);
        std::lock_guard lock(render_mutex);

        if (initialized.load()) return true;

        egl_manager = std::make_unique<gl::EGLManager>();
        if (!egl_manager->initialize(native_window)) {
            log_e("egl manager initialize failed");
            return false;
        }

        // The context will be made current on the render thread
        egl_manager->detach_current();
        initialized.store(true);
        log_i("VideoRenderer initialized successfully");
        return true;
    }

    auto VideoRenderer::setup_context() -> bool {
        std::lock_guard lock(render_mutex);
        if (!initialized.load() || !egl_manager) return false;

        if (!egl_manager->make_current()) {
            log_e("Failed to make EGL context current");
            return false;
        }

        if (!renderer) {
            renderer = gl::GLRenderer::create();
            renderer->initialize();
        }
        return true;
    }

    auto VideoRenderer::tear_down_context() -> void {
        std::lock_guard lock(render_mutex);
        if (renderer) {
            renderer->destroy();
            renderer.reset();
        }
        if (egl_manager) {
            egl_manager->detach_current();
        }
    }

    auto VideoRenderer::render_frame(AVFrame *frame) -> bool {
        std::lock_guard lock(render_mutex);

        if (!initialized.load() || !frame || !renderer) {
            return false;
        }
        return renderer->render_frame(frame);
    }

    auto VideoRenderer::swap_buffers() -> void {
        if (initialized.load() && egl_manager) {
            egl_manager->swap_buffers();
        }
    }

    auto VideoRenderer::set_viewport(int width, int height) -> void {
        std::lock_guard lock(render_mutex);
        if (renderer && initialized.load()) {
            renderer->set_viewport(width, height);
        }
    }

    auto VideoRenderer::destroy() -> void {
        std::lock_guard lock(render_mutex);

        if (!initialized.load()) return;

        // Ensure context is torn down
        tear_down_context();

        if (egl_manager) {
            egl_manager->destroy();
            egl_manager.reset();
        }

        initialized.store(false);
        log_i("VideoRenderer destroyed");
    }

}
