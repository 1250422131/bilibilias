#pragma once

#include <concepts>

namespace bilias::ffmpeg {

    template<typename F>
    requires std::is_invocable_v<F>
    struct Defer {
        F f;
        explicit Defer(F &&f) noexcept : f(std::move(f)) {}

        ~Defer() noexcept(noexcept(f())) {
            f();
        }
    };
} // namespace bilias::ffmpeg
