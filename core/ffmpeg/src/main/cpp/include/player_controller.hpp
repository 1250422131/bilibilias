#pragma once

#include "decoder.hpp"
#include "audio/audio_player.hpp"
#include "video_renderer.hpp"
#include <memory>
#include <string>
#include <atomic>

namespace bilias {

    enum class PlayerState {
        STOPPED,
        PLAYING,
        PAUSED,
        BUFFERING,
        END_OF_FILE
    };

    class PlayerController {
    public:
        PlayerController();
        ~PlayerController();

        PlayerController(const PlayerController &) = delete;
        PlayerController &operator=(const PlayerController &) = delete;

        void initialize(ANativeWindow *window, int fd);
        void destroy();

        void play();
        void pause();
        void stop();
        void seek(double time_in_seconds);

        void set_viewport(int width, int height);

    private:
        void video_render_loop();

        std::unique_ptr<Decoder> decoder_;
        std::unique_ptr<AudioPlayer> audio_player_;
        std::unique_ptr<VideoRenderer> video_renderer_;

        std::thread video_render_thread_;
        std::atomic<PlayerState> state_{PlayerState::STOPPED};
        std::atomic<bool> stop_flag_{false};

        // For AV sync
        double get_audio_clock();
        double get_video_clock();
    };

}
