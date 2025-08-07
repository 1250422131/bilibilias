#include <dynamic_lib_loader.hpp>
#include <memory>
#include <dlfcn.h>


namespace {
    auto get_android_lib() noexcept -> void * {
        static auto *lib = dlopen("libandroid.so", RTLD_LAZY);
        return lib;
    }

    bilias::AHardwareBuffer_allocate_t bilias_AHardwareBuffer_allocate = nullptr;
}

namespace bilias {

    auto init_hardware_buffer() noexcept -> bool {
        if (android_get_device_api_level() < 26) {
            return false;
        }
        auto *lib = get_android_lib();
        if (!lib) return false;

        auto *fn_AHardwareBuffer_allocate = reinterpret_cast<AHardwareBuffer_allocate_t>(dlsym(lib, "AHardwareBuffer_allocate"));
        if (!fn_AHardwareBuffer_allocate) {
            return false;
        }
        bilias_AHardwareBuffer_allocate = fn_AHardwareBuffer_allocate;

        return true;
    }

    auto get_AHardwareBuffer_allocate() noexcept -> AHardwareBuffer_allocate_t {
        return bilias_AHardwareBuffer_allocate;
    }
}
