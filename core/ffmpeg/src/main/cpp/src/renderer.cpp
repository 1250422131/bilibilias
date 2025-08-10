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
        log_i("VideoRenderer::initialize window: %ld, fd: %d", (long)native_window, fd);
        ffmpeg::debug_av1_support();

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

        egl_manager = std::make_unique<gl::EGLManager>();
        if (!egl_manager->initialize(native_window)) {
            log_e("egl manager initialize failed");
            return false;
        }

        egl_manager->make_current();

        renderer = gl::GLRenderer::create();
        renderer->initialize();

        decoder = std::make_unique<ffmpeg::FFmpegDecoder>(fd);
        decoder->init();

        initialized.store(true);

        egl_manager->detach_current();
        log_i("VideoRenderer initialized successfully");
        return true;

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
            log_e("render_frame failed: {}", e.what());
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
            log_i("VideoRenderer destroyed");

        } catch (const std::exception& e) {
            log_i("VideoRenderer destroy failed: {}", e.what());
            initialized.store(false);
        }

    }

    auto VideoRenderer::test_play() -> void {
        std::thread a([this] {
            try {
                log_i("VideoRenderer::test_play() thread");
                auto g = decoder->new_generator();
                auto success = egl_manager->make_current();
                if (!success) {
                    log_i("VideoRenderer::test_play make_current failed");
                    return;
                }
                auto defer = Defer([&] {
                   egl_manager->detach_current();
                });
                while (g.next()) {
                    auto *f = g.value();
                    success = renderer->render_frame(f);
                    if (success) {
                        egl_manager->swap_buffers();
                    }
                    // sleep(1);
                    usleep(33333 );
                }
            } catch (std::exception &e) {
                log_i("VideoRenderer::test_play() ex: {}", e.what());
            } catch (...) {
                log_i("VideoRenderer::test_play() unkown error");
            }
            log_i("VideoRenderer::test_play thread finished");
        });
        a.detach();
    }
}
