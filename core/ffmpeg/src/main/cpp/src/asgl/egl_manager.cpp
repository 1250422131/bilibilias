#include <asgl/egl_manager.hpp>
#include <jni_log.hpp>

namespace bilias::gl {


    EGLManager::EGLManager() = default;

    EGLManager::~EGLManager() {
        destroy();
    }

    auto EGLManager::initialize(ANativeWindow *window) -> bool {
        if (initialized) return true;

        native_window = window;


        if (!init_egl()) {
            LOGE("Failed to initialize EGL");
            cleanup_egl();
            return false;
        }
        if (!create_surface(window)) {
            LOGE("Failed to create EGL surface");
            cleanup_egl();
            return false;
        }

        if (!create_context()) {
            LOGE("Failed to create EGL context");
            cleanup_egl();
            return false;
        }

        initialized = true;
        return true;
    }

    auto EGLManager::make_current() -> void {
        if (initialized) {
            eglMakeCurrent(display, surface, surface, context);
        }

    }

    auto EGLManager::swap_buffers() -> void {
        if (initialized) {
            eglSwapBuffers(display, surface);
        }

    }

    auto EGLManager::destroy() -> void {
        if (initialized) {
            cleanup_egl();
            initialized = false;
        }

    }

    auto EGLManager::init_egl() -> bool {
        display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
        if (display == EGL_NO_DISPLAY) {
            LOGE("eglGetDisplay failed");
            return false;
        }
        EGLint major, minor;
        if (!eglInitialize(display, &major, &minor)) {
            LOGE("eglInitialize failed");
            return false;
        }

        constexpr EGLint config_attribs[] = {
                EGL_RENDERABLE_TYPE, EGL_OPENGL_ES3_BIT,
                EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
                EGL_BLUE_SIZE, 8,
                EGL_GREEN_SIZE, 8,
                EGL_RED_SIZE, 8,
                EGL_ALPHA_SIZE, 8,
                EGL_DEPTH_SIZE, 16,
                EGL_NONE
        };

        EGLint numConfigs;
        if (!eglChooseConfig(display, config_attribs, &config, 1, &numConfigs) || numConfigs != 1) {
            LOGE("eglChooseConfig failed");
            return false;
        }

        return true;
    }

    auto EGLManager::create_surface(ANativeWindow *window) -> bool {
        surface = eglCreateWindowSurface(display, config, window, nullptr);
        if (surface == EGL_NO_SURFACE) {
            LOGE("eglCreateWindowSurface failed: %d", eglGetError());
            return false;
        }
        return true;

    }

    auto EGLManager::create_context() -> bool {
        constexpr EGLint context_attribs[] = {
                EGL_CONTEXT_CLIENT_VERSION, 3,
                EGL_NONE
        };

        context = eglCreateContext(display, config, EGL_NO_CONTEXT, context_attribs);
        if (context == EGL_NO_CONTEXT) {
            LOGE("eglCreateContext failed: %d", eglGetError());
            return false;
        }
        return true;

    }

    auto EGLManager::cleanup_egl() -> void {
        if (display != EGL_NO_DISPLAY) {
            eglMakeCurrent(display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);

            if (context != EGL_NO_CONTEXT) {
                eglDestroyContext(display, context);
                context = EGL_NO_CONTEXT;
            }

            if (surface != EGL_NO_SURFACE) {
                eglDestroySurface(display, surface);
                surface = EGL_NO_SURFACE;
            }

            eglTerminate(display);
            display = EGL_NO_DISPLAY;
        }
    }

}
