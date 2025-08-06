#pragma once

#include <concepts>

namespace bilias {

    class NonCopy {
    public:
        constexpr NonCopy() = default;
        NonCopy(const NonCopy &) = delete;
        NonCopy &operator=(const NonCopy &) = delete;
    };

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
