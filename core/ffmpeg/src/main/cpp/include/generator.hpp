#pragma once

#include <optional>
#include <coroutine>
#include <exception>
#include <stdexcept>
#include <queue>
#include <mutex>

namespace bilias {

    template<typename T>
    class Generator;

    namespace detail {

        template<typename T>
        struct GeneratorPromise {
            std::optional<T> current_value{std::nullopt};
            std::exception_ptr ex{nullptr};

            auto get_return_object() -> Generator<T>;

            constexpr auto initial_suspend() const noexcept -> std::suspend_always {
                return {};
            }

            constexpr auto final_suspend() const noexcept -> std::suspend_always {
                return {};
            }

            auto unhandled_exception() noexcept -> void {
                this->ex = std::current_exception();
            }

            auto yield_value(T &&value) -> std::suspend_always {
                if (current_value.has_value()) {
                    throw std::runtime_error("Generator Already has value!");
                }
                current_value = std::move(value);
                return {};
            }

            constexpr auto return_void() const noexcept -> void {}

        };


    }

    template<typename T>
    class Generator {
    public:
        using promise_type = detail::GeneratorPromise<T>;

    private:
        std::coroutine_handle<promise_type> handle;

    public:
        explicit Generator(std::coroutine_handle<promise_type> handle) : handle(handle) {}

        ~Generator() {
            if (handle) {}
            handle.destroy();
        }

        auto next() -> bool {
            if (!handle.done()) {
                handle();
                return !handle.done();
            }
            return false;
        }

        auto value() -> T {
            return std::move(handle.promise().current_value).value();
        }
    };

    namespace detail {
        template<typename T>
        auto GeneratorPromise<T>::get_return_object() -> Generator<T> {
            return Generator<T>{std::coroutine_handle<GeneratorPromise>::from_promise(*this)};
        }
    }

    template<typename T>
    class BufferedGenerator;

    namespace detail {
        template<typename T>
        struct BufferedGeneratorPromise {
            std::exception_ptr ex{nullptr};
            std::queue<T> buffer{};
            std::mutex mtx{};
            std::condition_variable cv_producer{};
            std::condition_variable cv_consumer{};
            bool done{false};
            size_t max_buffered_frames = 60;

            auto get_return_object() -> BufferedGenerator<T>;

            constexpr auto initial_suspend() const noexcept -> std::suspend_always {
                return {};
            }

            auto final_suspend() noexcept -> std::suspend_always {
                std::lock_guard<std::mutex> lock(mtx);
                done = true;
                cv_producer.notify_all();
                cv_consumer.notify_all();

                return {};
            }

            auto unhandled_exception() noexcept -> void {
                this->ex = std::current_exception();
            }

            constexpr auto return_void() const noexcept -> void{}

            auto yield_value(T &&value) -> std::suspend_always {
                std::unique_lock lock(mtx);
                cv_producer.wait(lock, [this] {
                   return buffer.size() < max_buffered_frames || done;
                });
                if (done) return {};

                buffer.push(std::move(value));
                cv_consumer.notify_one();
                return {};
            }

            auto get_next() -> std::optional<T> {
                std::unique_lock lock(mtx);
                cv_consumer.wait(lock, [this] {
                   return !buffer.empty() || done;
                });

                if (buffer.empty()) {
                    return std::nullopt;
                }

                auto &&value = std::move(buffer.front());
                buffer.pop();
                if (buffer.size()  < max_buffered_frames) {
                    cv_producer.notify_one();
                }

                return std::move(value);
            }
        };

        struct BufferedGeneratorSizeSetter {

            size_t max_buffered_frames;

            constexpr explicit BufferedGeneratorSizeSetter(size_t size) : max_buffered_frames(size) {}

            constexpr auto await_ready() const noexcept -> bool  {
                return false;
            }

            template<typename Promise>
            auto await_suspend(std::coroutine_handle<Promise> handle) -> bool {
                auto &p = handle.promise();
                p.max_buffered_frames = this->max_buffered_frames;
                return false;
            }

            constexpr auto await_resume() const noexcept -> void {}
        };

    }

    template<typename T>
    class BufferedGenerator {
    public:
        using promise_type = detail::BufferedGeneratorPromise<T>;
    private:
        std::coroutine_handle<promise_type> handle;
        std::optional<T> current_value{std::nullopt};
    public:
        explicit BufferedGenerator(std::coroutine_handle<promise_type> handle) : handle(handle) {}

        ~BufferedGenerator() {
            auto& p = handle.promise();
            {
                std::lock_guard<std::mutex> lock(p.mtx);
                p.done = true;
            }
            p.cv_producer.notify_all();
            p.cv_consumer.notify_all();
            handle.destroy();

        }

        auto next() -> bool {
            auto &p = handle.promise();
            if (p.ex) {
                std::rethrow_exception(p.ex);
            }
            std::unique_lock lock(p.mtx);
            p.cv_consumer.wait(lock, [this] {
                return !p.buffer.empty() || p.done;
            });

            if (p.buffer.empty()) {
                current_value.reset();
                return false;
            }

            current_value = std::move(p.buffer.front());
            p.buffer.pop();

            if (p.buffer.size() == p.max_buffered_frames - 1) {
                p.cv_producer.notify_one();
            }
            return true;
        }

        auto value() -> T {
            if (!current_value) {
                throw std::runtime_error("No current value. Call next() first.");
            }
            return std::move(current_value).value();
        }
    };

    auto set_generator_buffer_size(size_t size) -> detail::BufferedGeneratorSizeSetter {
        return detail::BufferedGeneratorSizeSetter{size};
    }

    namespace detail {
        template<typename T>
        auto BufferedGeneratorPromise<T>::get_return_object() -> BufferedGenerator<T> {
            return BufferedGenerator<T>{std::coroutine_handle<BufferedGeneratorPromise>::from_promise(*this)};
        }
    }
}
