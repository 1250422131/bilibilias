#pragma once

extern "C" {
    #include <android/hardware_buffer.h>
}

namespace bilias {

    using AHardwareBuffer_allocate_t = int(*)(const AHardwareBuffer_Desc*, AHardwareBuffer**);

    auto init_hardware_buffer() noexcept -> bool;

    auto get_AHardwareBuffer_allocate() noexcept -> AHardwareBuffer_allocate_t;
}
