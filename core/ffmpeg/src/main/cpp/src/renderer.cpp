#include "video_renderer.hpp"
#include <mutex>
#include <jni_log.hpp>
#include <unistd.h>
#include <fcntl.h>

namespace {


}

namespace bilias {


    VideoRenderer::VideoRenderer() = default;

    VideoRenderer::~VideoRenderer() {
        destroy();
    }

    auto VideoRenderer::initialize(ANativeWindow *native_window, int fd) -> bool {
        LOGE("VideoRenderer::initialize window: %ld, fd: %d", (long)native_window, fd);

        if (fcntl(fd, F_GETFL) < 0) {
            auto msg = std::format("FD {} is invalid: {}", fd, strerror(errno));
            throw std::runtime_error(msg);
        }

        if (lseek64(fd, 0, SEEK_SET) < 0) { // 测试 seek 权限
            perror("lseek64 failed");
            throw std::runtime_error("FD seek failed");
        }


        std::lock_guard lock(render_mutex);

        if (initialized.load()) return true;

        try {
            egl_manager = std::make_unique<gl::EGLManager>();
            if (!egl_manager->initialize(native_window)) {
                LOGE("egl manager initialize failed");
                return false;
            }

            egl_manager->make_current();

            renderer = gl::GLRenderer::create();
            renderer->initialize();

            decoder = std::make_unique<ffmpeg::FFmpegDecoder>(fd);
            decoder->init();

            initialized.store(true);
            LOGI("VideoRenderer initialized successfully");
            return true;

        } catch (const std::exception& e) {
            LOGE("VideoRenderer initialization failed: %s", e.what());
            egl_manager.reset();
            renderer.reset();
            return false;
        }

    }

    auto VideoRenderer::render_frame(AVFrame *frame) -> bool {
        std::lock_guard lock(render_mutex);

        if (!initialized.load() || !frame || !egl_manager || !renderer) {
            return false;
        }
        try {
            egl_manager->make_current();
            auto success = renderer->render_frame(frame);
            if (success) {
                egl_manager->swap_buffers();
            }
            return success;

        } catch (const std::exception& e) {
            LOGE("render_frame failed: %s", e.what());
            return false;
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

        try {
            if (renderer) {
                renderer->destroy();
                renderer.reset();
            }

            if (egl_manager) {
                egl_manager->destroy();
                egl_manager.reset();
            }

            initialized.store(false);
            LOGI("VideoRenderer destroyed");

        } catch (const std::exception& e) {
            LOGE("VideoRenderer destroy failed: %s", e.what());
            initialized.store(false);
        }

    }
}
