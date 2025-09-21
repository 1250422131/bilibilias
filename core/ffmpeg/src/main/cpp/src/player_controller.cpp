#include "player_controller.hpp"
#include "jni_log.hpp"
#include <chrono>

extern "C" {
#include "libavutil/frame.h"
#include "libavutil/rational.h"
}

namespace bilias {

    PlayerController::PlayerController() = default;

    PlayerController::~PlayerController() {
        destroy();
    }

    void PlayerController::initialize(ANativeWindow *window, int fd) {
        decoder_ = std::make_unique<Decoder>(fd);
        audio_player_ = std::make_unique<AudioPlayer>(decoder_->audio_frame_queue);
        video_renderer_ = std::make_unique<VideoRenderer>();
        video_renderer_->initialize(window);
    }

    void PlayerController::destroy() {
        stop(); // Ensure all threads are stopped before destruction
        video_renderer_.reset();
        audio_player_.reset();
        decoder_.reset();
    }

    void PlayerController::play() {
        if (state_ == PlayerState::PLAYING) return;

        // Start the audio player
        auto audio_params = decoder_->get_audio_params();
        if (audio_params.sample_rate > 0) {
            audio_player_->start(audio_params);
        }

        // Start the decoder thread
        decoder_->start();

        // Start the video rendering thread
        stop_flag_.store(false);
        video_render_thread_ = std::thread(&PlayerController::video_render_loop, this);

        state_ = PlayerState::PLAYING;
        log_i("PlayerController started playing");
    }

    void PlayerController::pause() {
        // TODO: Implement pause logic
        state_ = PlayerState::PAUSED;
    }

    void PlayerController::stop() {
        if (state_ == PlayerState::STOPPED) return;

        stop_flag_.store(true);

        if (decoder_) {
            decoder_->stop();
        }
        if (video_render_thread_.joinable()) {
            video_render_thread_.join();
        }
        if (audio_player_) {
            audio_player_->stop();
        }

        state_ = PlayerState::STOPPED;
        log_i("PlayerController stopped");
    }

    void PlayerController::seek(double time_in_seconds) {
        // TODO: Implement seek logic
    }

    void PlayerController::set_viewport(int width, int height) {
        if (video_renderer_) {
            video_renderer_->set_viewport(width, height);
        }
    }

    void PlayerController::video_render_loop() {
        if (!video_renderer_->setup_context()) {
            log_e("Failed to setup render context in render loop");
            return;
        }

        AVRational video_time_base = decoder_->get_video_time_base();

        while (!stop_flag_.load()) {
            if (state_ == PlayerState::PAUSED) {
                std::this_thread::sleep_for(std::chrono::milliseconds(10));
                continue;
            }

            AVFrame *video_frame = nullptr;
            if (decoder_->video_frame_queue.try_pop(video_frame)) {
                double video_pts_sec = video_frame->pts * av_q2d(video_time_base);
                double audio_clock_sec = get_audio_clock();

                double delay = video_pts_sec - audio_clock_sec;

                if (delay > 0.0) {
                    // Video is ahead, wait
                    std::this_thread::sleep_for(std::chrono::milliseconds(static_cast<long>(delay * 1000)));
                } else if (delay < -0.1) {
                    // Video is too far behind, drop frame
                    log_i("Dropping video frame to catch up (delay: %f s)", delay);
                    av_frame_free(&video_frame);
                    continue;
                }

                // Render the frame
                video_renderer_->render_frame(video_frame);
                video_renderer_->swap_buffers();
                av_frame_free(&video_frame);
            } else {
                // Queue is empty, wait a bit
                std::this_thread::sleep_for(std::chrono::milliseconds(10));
            }
        }

        video_renderer_->tear_down_context();
        log_i("Video render loop finished");
    }

    double PlayerController::get_audio_clock() {
        if (audio_player_) {
            return audio_player_->get_clock();
        }
        return 0.0;
    }

}
