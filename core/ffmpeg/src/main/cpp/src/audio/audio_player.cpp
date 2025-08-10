#include <audio/audio_player.hpp>
#include <ffmpeg/ffmpeg_util.hpp>
#include <jni_log.hpp>

namespace bilias::audio {


    auto AudioPlayer::init() -> void {

    }

    auto AudioPlayer::create_engine() -> void {
        auto result = slCreateEngine(&engine_obj, 0, nullptr, 0, nullptr, nullptr);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("slCreateEngine failed");
        }
        result = (*engine_obj)->Realize(engine_obj, SL_BOOLEAN_FALSE);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*engine_obj)->Realize failed");
        }
        result = (*engine_obj)->GetInterface(engine_obj, SL_IID_ENGINE, &engine);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*engine_obj)->GetInterface failed");
        }
    }

    auto AudioPlayer::create_output_mix() -> void {
        auto result = (*engine)->CreateOutputMix(engine, &output_mix, 0, nullptr, nullptr);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*engine)->CreateOutputMix failed");
        }
        result = (*output_mix)->Realize(output_mix, SL_BOOLEAN_FALSE);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*output_mix)->Realize failed");
        }
    }

    auto AudioPlayer::create_audio_player() -> void {
        auto loc_bufq = SLDataLocator_AndroidSimpleBufferQueue {
            .locatorType = SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,
            .numBuffers = 2
        };
        auto loc_fmt = SLDataFormat_PCM {
            .formatType = SL_DATAFORMAT_PCM,
            .numChannels = static_cast<SLuint32>(channel_count),
            .samplesPerSec = static_cast<SLuint32>(sample_rate),
            .bitsPerSample = static_cast<SLuint32>(bits_per_sample),
            .containerSize = 0,
        };
        auto audio_src = SLDataSource {
            .pLocator = &loc_bufq,
            .pFormat = &loc_fmt
        };
        auto loc_outmix = SLDataLocator_OutputMix {
            .locatorType = SL_DATALOCATOR_OUTPUTMIX,
            .outputMix = output_mix
        };
        auto audio_sink = SLDataSink {
            .pLocator = &loc_outmix,
            .pFormat = nullptr
        };

        const SLInterfaceID ids[1] = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE};
        const SLboolean req[1] = {SL_BOOLEAN_TRUE};

        auto result = (*engine)->CreateAudioPlayer(engine, &player_obj, &audio_src, &audio_sink, 1, ids, req);

        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*engine)->CreateAudioPlayer failed");
        }
        result = (*player_obj)->Realize(player_obj, SL_BOOLEAN_FALSE);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*player_obj)->Realize failed");
        }
        result = (*player_obj)->GetInterface(player_obj, SL_IID_PLAY, &player);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*player_obj)->GetInterface failed");
        }
        result = (*player_obj)->GetInterface(player_obj, SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &player_queue);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*player_obj)->GetInterface failed");
        }
        result = (*player)->SetPlayState(player, SL_PLAYSTATE_PLAYING);
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*player)->SetPlayState failed");
        }
        result = (*player_queue)->RegisterCallback(player_queue, nullptr, this); // TODO
        if (result != SL_RESULT_SUCCESS) {
            throw std::runtime_error("(*player_queue)->RegisterCallback failed");
        }
    }

    AAudioPlayer::AAudioPlayer() {

    }

    AAudioPlayer::~AAudioPlayer() {

    }

    auto AAudioPlayer::init(int sample_rate, int channel_count) -> void {
        if (initialized.load()) return;
        this->sample_rate = sample_rate;
        this->channel_count = channel_count;

        AAudioStreamBuilder *builder{nullptr};
        auto builder_defer = Defer{[&] {
            if (builder) {
                AAudioStreamBuilder_delete(builder);
            }
        }};
        auto result = AAudio_createStreamBuilder(&builder);
        if (result != AAUDIO_OK) {
            throw std::runtime_error("AAudio_createStreamBuilder failed");
        }
        AAudioStreamBuilder_setDirection(builder, AAUDIO_DIRECTION_OUTPUT);
        AAudioStreamBuilder_setSharingMode(builder, AAUDIO_SHARING_MODE_EXCLUSIVE);
        AAudioStreamBuilder_setSampleRate(builder, sample_rate);
        AAudioStreamBuilder_setChannelCount(builder, channel_count);
        AAudioStreamBuilder_setFormat(builder, format);
        AAudioStreamBuilder_setPerformanceMode(builder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);

        // TODO callback
        AAudioStreamBuilder_setDataCallback(builder, nullptr, this);
        AAudioStreamBuilder_setErrorCallback(builder, nullptr, this);

        result = AAudioStreamBuilder_openStream(builder, &stream);
        if (result != AAUDIO_OK) {
            throw std::runtime_error("AAudioStreamBuilder_openStream failed");
        }

        max_frames_per_burst = AAudioStream_getFramesPerBurst(stream);

        log_i("AAudio stream created: {}Hz, {} channels, {} frames per burst", sample_rate, channel_count, max_frames_per_burst);
        initialized.store(true);
    }

    auto AAudioPlayer::play_frame(AVFrame *frame) -> void {

    }

    auto AAudioPlayer::start() -> bool {
        return false;
    }

    auto AAudioPlayer::stop() -> void {

    }
}
