#pragma once

#include <queue>
#include <mutex>
#include <condition_variable>

namespace bilias {

    template<typename T>
    class ThreadSafeQueue {
    public:
        ThreadSafeQueue() = default;

        ~ThreadSafeQueue() = default;

        ThreadSafeQueue(const ThreadSafeQueue &) = delete;

        ThreadSafeQueue &operator=(const ThreadSafeQueue &) = delete;

        void push(T new_value) {
            std::lock_guard<std::mutex> lock(mutex_);
            queue_.push(std::move(new_value));
            cond_var_.notify_one();
        }

        bool try_pop(T &value) {
            std::lock_guard<std::mutex> lock(mutex_);
            if (queue_.empty()) {
                return false;
            }
            value = std::move(queue_.front());
            queue_.pop();
            return true;
        }

        void wait_and_pop(T &value) {
            std::unique_lock<std::mutex> lock(mutex_);
            cond_var_.wait(lock, [this] { return !queue_.empty(); });
            value = std::move(queue_.front());
            queue_.pop();
        }

        bool empty() const {
            std::lock_guard<std::mutex> lock(mutex_);
            return queue_.empty();
        }

        size_t size() const {
            std::lock_guard<std::mutex> lock(mutex_);
            return queue_.size();
        }

        void clear() {
            std::lock_guard<std::mutex> lock(mutex_);
            std::queue<T> empty;
            queue_.swap(empty);
        }

    private:
        mutable std::mutex mutex_;
        std::queue<T> queue_;
        std::condition_variable cond_var_;
    };

}
