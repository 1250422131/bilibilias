package com.imcys.bilibilias.core.ffmpeg

import android.media.MediaMetadataRetriever
import androidx.core.net.toUri
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@HiltAndroidTest
class FFmpegKitImplTest {
    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Inject
    lateinit var ffmpegKitImpl: FFmpegKitImpl
    private lateinit var outFile: File

    @BeforeTest
    fun setup() {
        hiltRule.inject()
        outFile = tmpFolder.newFile("outputFile.mp4")
    }

    @Test
    fun test_verify_mix_command() {
        val assets = InstrumentationRegistry.getInstrumentation().context.assets
        val aFile = tmpFolder.newFile()
        copyInputStreamToFile(assets.open("audio.acc"), aFile)
        val vFile = tmpFolder.newFile()
        copyInputStreamToFile(assets.open("video.mp4"), vFile)

        val result =
            ffmpegKitImpl.generateCommand(
                "-y -i {input} -i {input} -vcodec copy -acodec copy {output}",
                outFile.toUri().toString(),
                arrayOf(
                    vFile.toUri().toString(),
                    aFile.toUri().toString(),
                ),
            )
        assertEquals(
            arrayOf(
                "-y",
                "-i",
                "saf:1.unknown",
                "-i",
                "saf:2.unknown",
                "-vcodec",
                "copy",
                "-acodec",
                "copy",
                "saf:3.unknown",
            ),
            result,
        )
    }

    @Test
    fun test_mix_video_with_audio_has_audio_track() {
        val assets = InstrumentationRegistry.getInstrumentation().context.assets
        val aFile = tmpFolder.newFile()
        copyInputStreamToFile(assets.open("audio.acc"), aFile)
        val vFile = tmpFolder.newFile()
        copyInputStreamToFile(assets.open("video.mp4"), vFile)

        ffmpegKitImpl.execute(
            "-y -i {input} -i {input} -vcodec copy -acodec copy {output}",
            outFile.toUri().toString(),
            vFile.toUri().toString(),
            aFile.toUri().toString(),
            onSuccess = {
                assertTrue { isVideoHaveAudioTrack(outFile.path) }
            },
            onFailure = {},
        )
    }

    private fun isVideoHaveAudioTrack(path: String): Boolean {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val hasAudioStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO)
        return hasAudioStr == "yes"
    }

    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        inputStream.copyTo(FileOutputStream(file))
    }
}
