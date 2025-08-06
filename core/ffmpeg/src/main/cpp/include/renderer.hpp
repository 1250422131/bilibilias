#pragma once

#include <string_view>
#include <thread>
#include <GLES3/gl3.h>
#include "traits.hpp"

extern "C" {
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
}


namespace bilias {

    class VideoRenderer final : NonCopy {

        // opengl stuff
        GLuint program;
        GLuint position_handle;
        GLuint texture_handle;
        GLuint vbo;
        GLuint vao;
        GLuint texture_id;

        // ffmpeg stuff
        AVIOContext *avio_ctx;
        AVFormatContext* format_ctx;
        AVCodecContext* codec_ctx;
        const AVCodec* codec;
        SwsContext* sws_ctx;
        AVFrame* frame;
        AVFrame* frame_rgb;
        AVPacket* packet;
        uint8_t* buffer;
        int video_stream_index;

        int video_width;
        int video_height;

        std::thread decode_thread;
        std::atomic<bool> is_playing;
        std::atomic<bool> should_stop;
        int video_fd{-1};

        std::mutex frame_mutex;
        bool has_new_frame;


    public:
        VideoRenderer();
        ~VideoRenderer();

        auto on_surface_created() -> void;
        auto on_surface_changed(int width, int height) -> void;
        auto on_draw_frame() -> void;
        auto set_video_fd(int fd) -> void;
        auto start_playback() -> void;
        auto pause_playback() -> void;


    private:
        auto create_shader(GLenum type, std::string_view  source) -> GLuint;
        auto create_program(std::string_view vertex_source, std::string_view fragment_source) -> GLuint;

        auto init_ffmpeg() -> bool;
        auto cleanup_ffmpeg() -> void;
        auto cleanup_opengl() -> void;
        auto decode_loop() -> void;
        auto setup_texture() -> void;
        auto update_texture() -> void;

    };
}