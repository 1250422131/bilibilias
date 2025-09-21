#pragma once

#include "traits.hpp"
#include "ffmpeg_util.hpp"
#include "audio/audio_player.hpp"
#include <stdexcept>
#include <functional>

namespace bilias::ffmpeg {

    class FFmpegDecoder final : NonCopy {
    private:
        int fd_;
        AVIOContextPtr avio_ctx_{nullptr};
        AVMallocPtr buffer_{nullptr};
        AVFormatContextPtr format_ctx_{nullptr};

        // Video
        AVCodecContextPtr video_codec_ctx_{nullptr};
        int video_stream_index_{-1};

        // Audio
        AVCodecContextPtr audio_codec_ctx_{nullptr};
        int audio_stream_index_{-1};

    public:
        explicit FFmpegDecoder(int fd);
        ~FFmpegDecoder();

        void init();

        // Reads the next packet from the stream
        int read_frame(AVPacket *packet);

        // Decodes a packet and invokes the callback with the resulting frame
        void decode_packet(AVPacket *packet, const std::function<void(AVFrame *)> &on_frame_decoded);

        // Getters for stream info
        int get_video_stream_index() const { return video_stream_index_; }
        int get_audio_stream_index() const { return audio_stream_index_; }
        AVRational get_video_time_base() const;
        AudioParams get_audio_params() const;
    };
}
