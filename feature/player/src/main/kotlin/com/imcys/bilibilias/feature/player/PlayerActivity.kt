package com.imcys.bilibilias.feature.player

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.media.AudioManager
import android.media.audiofx.LoudnessEnhancer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.util.TypedValue
import android.view.KeyEvent
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.view.accessibility.CaptioningManager
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.CaptionStyleCompat
import androidx.media3.ui.PlayerView
import androidx.media3.ui.SubtitleView
import androidx.media3.ui.TimeBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imcys.bilibilias.core.utils.convertToUTF8
import com.imcys.bilibilias.core.utils.deleteFiles
import com.imcys.bilibilias.core.utils.getFilenameFromUri
import com.imcys.bilibilias.core.utils.getMediaContentUri
import com.imcys.bilibilias.core.utils.isDeviceTvBox
import com.imcys.bilibilias.core.utils.subtitleCacheDir
import com.imcys.bilibilias.feature.player.dialog.PlaybackSpeedControlsDialogFragment
import com.imcys.bilibilias.feature.player.dialog.TrackSelectionDialogFragment
import com.imcys.bilibilias.feature.player.dialog.VideoZoomOptionsDialogFragment
import com.imcys.bilibilias.feature.player.dialog.nameRes
import com.imcys.bilibilias.feature.player.extensions.audioSessionId
import com.imcys.bilibilias.feature.player.extensions.getLocalSubtitles
import com.imcys.bilibilias.feature.player.extensions.getSubtitleMime
import com.imcys.bilibilias.feature.player.extensions.isPortrait
import com.imcys.bilibilias.feature.player.extensions.next
import com.imcys.bilibilias.feature.player.extensions.prettyPrintIntent
import com.imcys.bilibilias.feature.player.extensions.seekBack
import com.imcys.bilibilias.feature.player.extensions.seekForward
import com.imcys.bilibilias.feature.player.extensions.setImageDrawable
import com.imcys.bilibilias.feature.player.extensions.skipSilenceEnabled
import com.imcys.bilibilias.feature.player.extensions.switchTrack
import com.imcys.bilibilias.feature.player.extensions.toSubtitle
import com.imcys.bilibilias.feature.player.extensions.toTypeface
import com.imcys.bilibilias.feature.player.extensions.togglePlayPause
import com.imcys.bilibilias.feature.player.extensions.toggleSystemBars
import com.imcys.bilibilias.feature.player.model.Font
import com.imcys.bilibilias.feature.player.model.ScreenOrientation
import com.imcys.bilibilias.feature.player.model.Subtitle
import com.imcys.bilibilias.feature.player.model.VideoZoom
import com.imcys.bilibilias.feature.player.util.BrightnessManager
import com.imcys.bilibilias.feature.player.util.PlayerApi
import com.imcys.bilibilias.feature.player.util.PlayerGestureHelper
import com.imcys.bilibilias.feature.player.util.PlaylistManager
import com.imcys.bilibilias.feature.player.util.Utils
import com.imcys.bilibilias.feature.player.util.VolumeManager
import com.imcys.bilibilias.feature.player.util.toMillis
import dagger.hilt.android.AndroidEntryPoint
import io.github.aakira.napier.Napier
import io.github.anilbeesetti.nextlib.media3ext.ffdecoder.NextRenderersFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModels<PlayerViewModel>()

    private var playWhenReady = true
    private var isPlaybackFinished = false

    var isFileLoaded = false
    var isControlsLocked = false
    private var shouldFetchPlaylist = true
    private var isSubtitleLauncherHasUri = false
    private var isFirstFrameRendered = false
    private var isFrameRendered = false
    private var isPlayingOnScrubStart: Boolean = false
    private var previousScrubPosition = 0L
    private var scrubStartPosition: Long = -1L
    private var currentOrientation: Int? = null
    private var currentVideoOrientation: Int? = null
    var currentVideoSize: VideoSize? = null
    private var hideVolumeIndicatorJob: Job? = null
    private var hideBrightnessIndicatorJob: Job? = null
    private var hideInfoLayoutJob: Job? = null

    private val shouldFastSeek: Boolean
        get() = true

    /**
     * Player
     */
    private lateinit var player: Player
    private lateinit var playerGestureHelper: PlayerGestureHelper
    private lateinit var playlistManager: PlaylistManager
    private lateinit var trackSelector: DefaultTrackSelector
    private var surfaceView: SurfaceView? = null
    private var mediaSession: MediaSession? = null
    private lateinit var playerApi: PlayerApi
    private lateinit var volumeManager: VolumeManager
    private lateinit var brightnessManager: BrightnessManager
    var loudnessEnhancer: LoudnessEnhancer? = null

    /**
     * Listeners
     */
    private val playbackStateListener: Player.Listener = playbackStateListener()
    private val subtitleFileLauncher = registerForActivityResult(OpenDocument()) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            isSubtitleLauncherHasUri = true
        }
        playVideo(playlistManager.getCurrent() ?: intent.data!!)
    }

    /**
     * Player controller views
     */
    private lateinit var audioTrackButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var exoContentFrameLayout: AspectRatioFrameLayout
    private lateinit var lockControlsButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var playbackSpeedButton: ImageButton
    private lateinit var playerLockControls: FrameLayout
    private lateinit var playerUnlockControls: FrameLayout
    private lateinit var playerCenterControls: LinearLayout
    private lateinit var prevButton: ImageButton
    private lateinit var screenRotateButton: ImageButton
    private lateinit var pipButton: ImageButton
    private lateinit var seekBar: TimeBar
    private lateinit var subtitleTrackButton: ImageButton
    private lateinit var unlockControlsButton: ImageButton
    private lateinit var videoTitleTextView: TextView
    private lateinit var videoZoomButton: ImageButton

    private lateinit var topInfoLayout: LinearLayout
    private lateinit var fastSpeedImage: ImageView
    private lateinit var topInfoText: TextView

    private lateinit var playerView: PlayerView

    private lateinit var infoLayout: LinearLayout
    private lateinit var infoText: TextView
    private lateinit var infoSubtext: TextView

    private lateinit var volumeGestureLayout: LinearLayout
    private lateinit var volumeProgressText: TextView
    private lateinit var volumeProgressBar: ProgressBar
    private lateinit var volumeImage: ImageView

    private lateinit var brightnessGestureLayout: LinearLayout
    private lateinit var brightnessProgressText: TextView
    private lateinit var brightnessProgressBar: ProgressBar
    private lateinit var brightnessIcon: ImageView

    private val isPipSupported: Boolean by lazy {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && packageManager.hasSystemFeature(
            PackageManager.FEATURE_PICTURE_IN_PICTURE,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prettyPrintIntent()

        // The window is always allowed to extend into the DisplayCutout areas on the short edges of the screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_player)

        // Initializing views
        audioTrackButton = findViewById(R.id.btn_audio_track)
        backButton = findViewById(R.id.back_button)
        exoContentFrameLayout = findViewById(R.id.exo_content_frame)
        lockControlsButton = findViewById(R.id.btn_lock_controls)
        nextButton = findViewById(R.id.btn_play_next)
        playbackSpeedButton = findViewById(R.id.btn_playback_speed)
        playerLockControls = findViewById(R.id.player_lock_controls)
        playerUnlockControls = findViewById(R.id.player_unlock_controls)
        playerCenterControls = findViewById(R.id.player_center_controls)
        prevButton = findViewById(R.id.btn_play_prev)
        screenRotateButton = findViewById(R.id.screen_rotate)
        pipButton = findViewById(R.id.btn_pip)
        seekBar = findViewById(R.id.exo_progress)
        subtitleTrackButton = findViewById(R.id.btn_subtitle_track)
        unlockControlsButton = findViewById(R.id.btn_unlock_controls)
        videoTitleTextView = findViewById(R.id.video_name)
        videoZoomButton = findViewById(R.id.btn_video_zoom)

        topInfoLayout = findViewById(R.id.top_info_layout)
        fastSpeedImage = findViewById(R.id.fast_speed_image)
        topInfoText = findViewById(R.id.top_info_text)

        playerView = findViewById(R.id.player_view)

        infoLayout = findViewById(R.id.info_layout)
        infoText = findViewById(R.id.info_text)
        infoSubtext = findViewById(R.id.info_subtext)

        volumeGestureLayout = findViewById(R.id.volume_gesture_layout)
        volumeProgressText = findViewById(R.id.volume_progress_text)
        volumeProgressBar = findViewById(R.id.volume_progress_bar)
        volumeImage = findViewById(R.id.volume_image)

        brightnessGestureLayout = findViewById(R.id.brightness_gesture_layout)
        brightnessProgressText = findViewById(R.id.brightness_progress_text)
        brightnessProgressBar = findViewById(R.id.brightness_progress_bar)
        brightnessIcon = findViewById(R.id.brightness_icon)
        if (!isPipSupported) {
            pipButton.visibility = View.GONE
        }

        seekBar.addListener(
            object : TimeBar.OnScrubListener {
                override fun onScrubStart(timeBar: TimeBar, position: Long) {
                    if (player.isPlaying) {
                        isPlayingOnScrubStart = true
                        player.pause()
                    }
                    isFrameRendered = true
                    scrubStartPosition = player.currentPosition
                    previousScrubPosition = player.currentPosition
                    scrub(position)
                    showPlayerInfo(
                        info = Utils.formatDurationMillis(position),
                        subInfo = "[${Utils.formatDurationMillisSign(position - scrubStartPosition)}]",
                    )
                }

                override fun onScrubMove(timeBar: TimeBar, position: Long) {
                    scrub(position)
                    showPlayerInfo(
                        info = Utils.formatDurationMillis(position),
                        subInfo = "[${Utils.formatDurationMillisSign(position - scrubStartPosition)}]",
                    )
                }

                override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                    hidePlayerInfo(0L)
                    scrubStartPosition = -1L
                    if (isPlayingOnScrubStart) {
                        player.play()
                    }
                }
            },
        )

        volumeManager =
            VolumeManager(audioManager = getSystemService(AUDIO_SERVICE) as AudioManager)
        brightnessManager = BrightnessManager(activity = this)
        playerGestureHelper = PlayerGestureHelper(
            playerView = playerView,
            activity = this,
            volumeManager = volumeManager,
            brightnessManager = brightnessManager,
        )

        playlistManager = PlaylistManager()
        playerApi = PlayerApi(this)
    }

    override fun onStart() {
        createPlayer()
        setOrientation()
        initPlaylist()
        initializePlayerView()
        playVideo(uri = playlistManager.getCurrent() ?: intent.data!!)
        super.onStart()
    }

    override fun onStop() {
        volumeGestureLayout.visibility = View.GONE
        brightnessGestureLayout.visibility = View.GONE
        currentOrientation = requestedOrientation
        releasePlayer()
        super.onStop()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            isPipSupported &&
            player.isPlaying &&
            !isControlsLocked
        ) {
            try {
                this.enterPictureInPictureMode(updatePictureInPictureParams())
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration,
    ) {
        if (isInPictureInPictureMode) {
            playerView.subtitleView?.setFractionalTextSize(SubtitleView.DEFAULT_TEXT_SIZE_FRACTION)
            playerUnlockControls.visibility = View.INVISIBLE
        } else {
            playerView.subtitleView?.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            if (!isControlsLocked) {
                playerUnlockControls.visibility = View.VISIBLE
            }
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePictureInPictureParams(): PictureInPictureParams {
        val params: PictureInPictureParams = PictureInPictureParams.Builder()
            .setAspectRatio(Rational(16, 9))
            .build()

        setPictureInPictureParams(params)
        return params
    }

    private fun createPlayer() {
        Napier.d("Creating player")

        val renderersFactory = NextRenderersFactory(applicationContext)
            .setEnableDecoderFallback(true)
            .setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER)

        trackSelector = DefaultTrackSelector(applicationContext).apply {
            this.setParameters(
                this.buildUponParameters()
                    .setPreferredAudioLanguage("")
                    .setPreferredTextLanguage(""),
            )
        }

        player = ExoPlayer.Builder(applicationContext)
            .setRenderersFactory(renderersFactory)
            .setTrackSelector(trackSelector)
            .setAudioAttributes(getAudioAttributes(), true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        try {
            if (player.canAdvertiseSession()) {
                mediaSession = MediaSession.Builder(this, player).build()
            }
            loudnessEnhancer = LoudnessEnhancer(player.audioSessionId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        player.addListener(playbackStateListener)
        volumeManager.loudnessEnhancer = loudnessEnhancer
    }

    private fun setOrientation() {
        currentOrientation?.let {
            requestedOrientation = it
        }
    }

    private fun initializePlayerView() {
        playerView.apply {
            setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
            player = this@PlayerActivity.player
            controllerShowTimeoutMs = 2.toMillis
            setControllerVisibilityListener(
                PlayerView.ControllerVisibilityListener { visibility ->
                    toggleSystemBars(showBars = visibility == View.VISIBLE && !isControlsLocked)
                },
            )

            subtitleView?.apply {
                val captioningManager = getSystemService(CAPTIONING_SERVICE) as CaptioningManager
                if (false) {
                    val systemCaptionStyle =
                        CaptionStyleCompat.createFromCaptionStyle(captioningManager.userStyle)
                    setStyle(systemCaptionStyle)
                } else {
                    val userStyle = CaptionStyleCompat(
                        Color.WHITE,
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                        CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW,
                        Color.BLACK,
                        Typeface.create(Font.DEFAULT.toTypeface(), Typeface.BOLD),
                    )
                    setStyle(userStyle)
                    setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                }
                setApplyEmbeddedStyles(true)
            }
        }

        audioTrackButton.setOnClickListener {
            trackSelector.currentMappedTrackInfo ?: return@setOnClickListener

            TrackSelectionDialogFragment(
                type = C.TRACK_TYPE_AUDIO,
                tracks = player.currentTracks,
                onTrackSelected = { player.switchTrack(C.TRACK_TYPE_AUDIO, it) },
            ).show(supportFragmentManager, "TrackSelectionDialog")
        }

        subtitleTrackButton.setOnClickListener {
            trackSelector.currentMappedTrackInfo ?: return@setOnClickListener

            TrackSelectionDialogFragment(
                type = C.TRACK_TYPE_TEXT,
                tracks = player.currentTracks,
                onTrackSelected = { player.switchTrack(C.TRACK_TYPE_TEXT, it) },
                onOpenLocalTrackClicked = {
                    subtitleFileLauncher.launch(
                        arrayOf(
                            MimeTypes.APPLICATION_SUBRIP,
                            MimeTypes.APPLICATION_TTML,
                            MimeTypes.TEXT_VTT,
                            MimeTypes.TEXT_SSA,
                            MimeTypes.BASE_TYPE_APPLICATION + "/octet-stream",
                            MimeTypes.BASE_TYPE_TEXT + "/*",
                        ),
                    )
                },
            ).show(supportFragmentManager, "TrackSelectionDialog")
        }

        playbackSpeedButton.setOnClickListener {
            PlaybackSpeedControlsDialogFragment(
                currentSpeed = player.playbackParameters.speed,
                skipSilenceEnabled = player.skipSilenceEnabled,
                onChange = {
                    player.setPlaybackSpeed(it)
                },
                onSkipSilenceChanged = {
                    player.skipSilenceEnabled = it
                },
            ).show(supportFragmentManager, "PlaybackSpeedSelectionDialog")
        }

        nextButton.setOnClickListener {
            if (playlistManager.hasNext()) {
                playlistManager.getCurrent()?.let { savePlayerState(it) }
                playVideo(playlistManager.getNext()!!)
            }
        }
        prevButton.setOnClickListener {
            if (playlistManager.hasPrev()) {
                playlistManager.getCurrent()?.let { savePlayerState(it) }
                playVideo(playlistManager.getPrev()!!)
            }
        }
        lockControlsButton.setOnClickListener {
            playerUnlockControls.visibility = View.INVISIBLE
            playerLockControls.visibility = View.VISIBLE
            isControlsLocked = true
            toggleSystemBars(showBars = false)
        }
        unlockControlsButton.setOnClickListener {
            playerLockControls.visibility = View.INVISIBLE
            playerUnlockControls.visibility = View.VISIBLE
            isControlsLocked = false
            playerView.showController()
            toggleSystemBars(showBars = true)
        }
        videoZoomButton.setOnClickListener {
            val videoZoom = VideoZoom.BEST_FIT.next()
            applyVideoZoom(videoZoom = videoZoom, showInfo = true)
        }

        videoZoomButton.setOnLongClickListener {
            VideoZoomOptionsDialogFragment(
                currentVideoZoom = VideoZoom.BEST_FIT,
                onVideoZoomOptionSelected = { applyVideoZoom(videoZoom = it, showInfo = true) },
            ).show(supportFragmentManager, "VideoZoomOptionsDialog")
            true
        }
        screenRotateButton.setOnClickListener {
            requestedOrientation = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
        }
        pipButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isPipSupported) {
                this.enterPictureInPictureMode(updatePictureInPictureParams())
            }
        }
        backButton.setOnClickListener { finish() }
    }

    private fun initPlaylist() = lifecycleScope.launch(Dispatchers.IO) {
        val mediaUri = getMediaContentUri(intent.data!!)

        if (mediaUri != null) {
            val playlist = viewModel.getPlaylistFromUri(mediaUri)
            playlistManager.setPlaylist(playlist)
        }
    }

    private fun playVideo(uri: Uri) = lifecycleScope.launch(Dispatchers.IO) {
        playlistManager.updateCurrent(uri)
        val isCurrentUriIsFromIntent = intent.data == uri

        viewModel.initMediaState(uri.toString())
        if (isCurrentUriIsFromIntent && playerApi.hasPosition && viewModel.currentPlaybackPosition == null) {
            viewModel.currentPlaybackPosition = playerApi.position?.toLong()
        }

        // Get all subtitles for current uri
        val apiSubs = if (isCurrentUriIsFromIntent) playerApi.getSubs() else emptyList()
        val localSubs =
            uri.getLocalSubtitles(this@PlayerActivity, viewModel.externalSubtitles.toList())
        val externalSubs = viewModel.externalSubtitles.map { it.toSubtitle(this@PlayerActivity) }

        // current uri as MediaItem with subs
        val subtitleStreams = createExternalSubtitleStreams(apiSubs + localSubs + externalSubs)
        val mediaStream = createMediaStream(uri).buildUpon()
            .setSubtitleConfigurations(subtitleStreams)
            .build()

        withContext(Dispatchers.Main) {
            surfaceView = SurfaceView(this@PlayerActivity).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            }
            player.setVideoSurfaceView(surfaceView)
            exoContentFrameLayout.addView(surfaceView, 0)

            if (isCurrentUriIsFromIntent && playerApi.hasTitle) {
                videoTitleTextView.text = playerApi.title
            } else {
                videoTitleTextView.text = getFilenameFromUri(uri)
            }

            // Set media and start player
            player.setMediaItem(mediaStream, viewModel.currentPlaybackPosition ?: C.TIME_UNSET)
            player.playWhenReady = playWhenReady
            player.prepare()
        }
    }

    private fun releasePlayer() {
        Napier.d("Releasing player")
        subtitleCacheDir.deleteFiles()
        playWhenReady = player.playWhenReady
        playlistManager.getCurrent()?.let { savePlayerState(it) }
        player.removeListener(playbackStateListener)
        player.release()
        mediaSession?.release()
        mediaSession = null
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            playerView.keepScreenOn = isPlaying
            super.onIsPlayingChanged(isPlaying)
        }

        override fun onAudioSessionIdChanged(audioSessionId: Int) {
            super.onAudioSessionIdChanged(audioSessionId)
            loudnessEnhancer?.release()

            try {
                loudnessEnhancer = LoudnessEnhancer(audioSessionId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            currentVideoSize = videoSize
            applyVideoZoom(videoZoom = VideoZoom.BEST_FIT, showInfo = false)
            exoContentFrameLayout.scaleX = viewModel.currentVideoScale
            exoContentFrameLayout.scaleY = viewModel.currentVideoScale
            exoContentFrameLayout.requestLayout()

            if (currentOrientation != null) return

            if (ScreenOrientation.VIDEO_ORIENTATION == ScreenOrientation.VIDEO_ORIENTATION) {
                currentVideoOrientation = if (videoSize.isPortrait) {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
                requestedOrientation = currentVideoOrientation!!
            }
            super.onVideoSizeChanged(videoSize)
        }

        override fun onPlayerError(error: PlaybackException) {
            Napier.e(error) { "onPlayerError" }
            val alertDialog = MaterialAlertDialogBuilder(this@PlayerActivity).apply {
                setTitle(getString(R.string.error_playing_video))
                setMessage(error.message ?: getString(R.string.unknown_error))
                setNegativeButton(getString(R.string.exit)) { _, _ ->
                    finish()
                }
                if (playlistManager.hasNext()) {
                    setPositiveButton(getString(R.string.play_next_video)) { dialog, _ ->
                        dialog.dismiss()
                        playVideo(playlistManager.getNext()!!)
                    }
                }
            }.create()

            alertDialog.show()
            super.onPlayerError(error)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    Napier.d("Player state: ENDED")
                    isPlaybackFinished = true
                    if (playlistManager.hasNext()) {
                        playlistManager.getCurrent()?.let { savePlayerState(it) }
                        viewModel.resetAllToDefaults()
                        playVideo(playlistManager.getNext()!!)
                    } else {
                        finish()
                    }
                }

                Player.STATE_READY -> {
                    Napier.d("Player state: READY")
                    Napier.d(playlistManager.toString())
                    isFrameRendered = true
                    isFileLoaded = true
                }

                Player.STATE_BUFFERING -> {
                    Napier.d("Player state: BUFFERING")
                }

                Player.STATE_IDLE -> {
                    Napier.d("Player state: IDLE")
                }
            }
            super.onPlaybackStateChanged(playbackState)
        }

        override fun onRenderedFirstFrame() {
            isFirstFrameRendered = true
            playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
            super.onRenderedFirstFrame()
        }

        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            if (isFirstFrameRendered) return

            if (isSubtitleLauncherHasUri) {
                val textTracks = player.currentTracks.groups
                    .filter { it.type == C.TRACK_TYPE_TEXT && it.isSupported }
                viewModel.currentSubtitleTrackIndex = textTracks.size - 1
            }
            isSubtitleLauncherHasUri = false
            player.switchTrack(C.TRACK_TYPE_AUDIO, viewModel.currentAudioTrackIndex)
            player.switchTrack(C.TRACK_TYPE_TEXT, viewModel.currentSubtitleTrackIndex)
            player.setPlaybackSpeed(viewModel.currentPlaybackSpeed)
            player.skipSilenceEnabled = viewModel.skipSilenceEnabled
        }
    }

    override fun finish() {
        if (playerApi.shouldReturnResult) {
            val result = playerApi.getResult(
                isPlaybackFinished = isPlaybackFinished,
                duration = player.duration,
                position = player.currentPosition,
            )
            setResult(RESULT_OK, result)
        }
        super.finish()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        playlistManager.clearQueue()
        viewModel.resetAllToDefaults()
        setIntent(intent)
        prettyPrintIntent()
        shouldFetchPlaylist = true
        playVideo(intent.data!!)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_DPAD_UP,
                -> {
                if (!playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    volumeManager.increaseVolume(true)
                    showVolumeGestureLayout()
                    return true
                }
            }

            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_DPAD_DOWN,
                -> {
                if (!playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    volumeManager.decreaseVolume(true)
                    showVolumeGestureLayout()
                    return true
                }
            }

            KeyEvent.KEYCODE_MEDIA_PLAY,
            KeyEvent.KEYCODE_MEDIA_PAUSE,
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
            KeyEvent.KEYCODE_BUTTON_SELECT,
                -> {
                when {
                    keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE -> player.pause()
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY -> player.play()
                    player.isPlaying -> player.pause()
                    else -> player.play()
                }
                return true
            }

            KeyEvent.KEYCODE_BUTTON_START,
            KeyEvent.KEYCODE_BUTTON_A,
            KeyEvent.KEYCODE_SPACE,
                -> {
                if (!playerView.isControllerFullyVisible) {
                    playerView.togglePlayPause()
                    return true
                }
            }

            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_BUTTON_L2,
            KeyEvent.KEYCODE_MEDIA_REWIND,
                -> {
                if (!playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                    val pos = player.currentPosition
                    if (scrubStartPosition == -1L) {
                        scrubStartPosition = pos
                    }
                    val position = (pos - 10_000).coerceAtLeast(0L)
                    player.seekBack(position, shouldFastSeek)
                    showPlayerInfo(
                        info = Utils.formatDurationMillis(position),
                        subInfo = "[${Utils.formatDurationMillisSign(position - scrubStartPosition)}]",
                    )
                    return true
                }
            }

            KeyEvent.KEYCODE_DPAD_RIGHT,
            KeyEvent.KEYCODE_BUTTON_R2,
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
                -> {
                if (!playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                    val pos = player.currentPosition
                    if (scrubStartPosition == -1L) {
                        scrubStartPosition = pos
                    }

                    val position = (pos + 10_000).coerceAtMost(player.duration)
                    player.seekForward(position, shouldFastSeek)
                    showPlayerInfo(
                        info = Utils.formatDurationMillis(position),
                        subInfo = "[${Utils.formatDurationMillisSign(position - scrubStartPosition)}]",
                    )
                    return true
                }
            }

            KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_NUMPAD_ENTER,
                -> {
                if (!playerView.isControllerFullyVisible) {
                    playerView.showController()
                    return true
                }
            }

            KeyEvent.KEYCODE_BACK -> {
                if (playerView.isControllerFullyVisible && player.isPlaying && isDeviceTvBox()) {
                    playerView.hideController()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
                -> {
                hideVolumeGestureLayout()
                return true
            }

            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_BUTTON_L2,
            KeyEvent.KEYCODE_MEDIA_REWIND,
            KeyEvent.KEYCODE_DPAD_RIGHT,
            KeyEvent.KEYCODE_BUTTON_R2,
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
                -> {
                hidePlayerInfo()
                return true
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun getAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
    }

    private fun scrub(position: Long) {
        if (isFrameRendered) {
            isFrameRendered = false
            if (position > previousScrubPosition) {
                player.seekForward(position, shouldFastSeek)
            } else {
                player.seekBack(position, shouldFastSeek)
            }
            previousScrubPosition = position
        }
    }

    fun showVolumeGestureLayout() {
        hideVolumeIndicatorJob?.cancel()
        volumeGestureLayout.visibility = View.VISIBLE
        volumeProgressBar.max = volumeManager.maxVolume.times(100)
        volumeProgressBar.progress = volumeManager.currentVolume.times(100).toInt()
        volumeProgressText.text = volumeManager.volumePercentage.toString()
    }

    fun showBrightnessGestureLayout() {
        hideBrightnessIndicatorJob?.cancel()
        brightnessGestureLayout.visibility = View.VISIBLE
        brightnessProgressBar.max = brightnessManager.maxBrightness.times(100).toInt()
        brightnessProgressBar.progress = brightnessManager.currentBrightness.times(100).toInt()
        brightnessProgressText.text = brightnessManager.brightnessPercentage.toString()
    }

    fun showPlayerInfo(info: String, subInfo: String? = null) {
        hideInfoLayoutJob?.cancel()

        infoLayout.visibility = View.VISIBLE
        infoText.text = info
        infoSubtext.visibility = View.GONE.takeIf { subInfo == null } ?: View.VISIBLE
        infoSubtext.text = subInfo
    }

    fun showTopInfo(info: String) {
        topInfoLayout.visibility = View.VISIBLE
        topInfoText.text = info
    }

    fun hideVolumeGestureLayout(delayTimeMillis: Long = HIDE_DELAY_MILLIS) {
        if (volumeGestureLayout.visibility != View.VISIBLE) return
        hideVolumeIndicatorJob = lifecycleScope.launch {
            delay(delayTimeMillis)
            volumeGestureLayout.visibility = View.GONE
        }
    }

    fun hideBrightnessGestureLayout(delayTimeMillis: Long = HIDE_DELAY_MILLIS) {
        if (brightnessGestureLayout.visibility != View.VISIBLE) return
        hideBrightnessIndicatorJob = lifecycleScope.launch {
            delay(delayTimeMillis)
            brightnessGestureLayout.visibility = View.GONE
        }
    }

    fun hidePlayerInfo(delayTimeMillis: Long = HIDE_DELAY_MILLIS) {
        if (infoLayout.visibility != View.VISIBLE) return
        hideInfoLayoutJob = lifecycleScope.launch {
            delay(delayTimeMillis)
            infoLayout.visibility = View.GONE
        }
    }

    fun hideTopInfo() {
        topInfoLayout.visibility = View.GONE
    }

    private fun savePlayerState(uri: Uri) {
        if (isFirstFrameRendered) {
        }
        isFirstFrameRendered = false
    }

    private fun createMediaStream(uri: Uri) = MediaItem.Builder()
        .setMediaId(uri.toString())
        .setUri(uri)
        .build()

    private suspend fun createExternalSubtitleStreams(subtitles: List<Subtitle>): List<MediaItem.SubtitleConfiguration> {
        return subtitles.map {
            MediaItem.SubtitleConfiguration.Builder(
                convertToUTF8(
                    uri = it.uri,
                    charset = null,
                ),
            ).apply {
                setId(it.uri.toString())
                setMimeType(it.uri.getSubtitleMime())
                setLabel(it.name)
                if (it.isSelected) setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
            }.build()
        }
    }

    private fun resetExoContentFrameWidthAndHeight() {
        exoContentFrameLayout.layoutParams.width = LayoutParams.MATCH_PARENT
        exoContentFrameLayout.layoutParams.height = LayoutParams.MATCH_PARENT
        exoContentFrameLayout.scaleX = 1.0f
        exoContentFrameLayout.scaleY = 1.0f
        exoContentFrameLayout.requestLayout()
    }

    private fun applyVideoZoom(videoZoom: VideoZoom, showInfo: Boolean) {
        viewModel.setVideoZoom(videoZoom)
        resetExoContentFrameWidthAndHeight()
        when (videoZoom) {
            VideoZoom.BEST_FIT -> {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                videoZoomButton.setImageDrawable(this, R.drawable.ic_fit_screen)
            }

            VideoZoom.STRETCH -> {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                videoZoomButton.setImageDrawable(this, R.drawable.ic_aspect_ratio)
            }

            VideoZoom.CROP -> {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                videoZoomButton.setImageDrawable(this, R.drawable.ic_crop_landscape)
            }

            VideoZoom.HUNDRED_PERCENT -> {
                currentVideoSize?.let {
                    exoContentFrameLayout.layoutParams.width = it.width
                    exoContentFrameLayout.layoutParams.height = it.height
                    exoContentFrameLayout.requestLayout()
                }
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                videoZoomButton.setImageDrawable(this, R.drawable.ic_width_wide)
            }
        }
        if (showInfo) {
            lifecycleScope.launch {
                infoLayout.visibility = View.VISIBLE
                infoText.text = getString(videoZoom.nameRes())
                delay(HIDE_DELAY_MILLIS)
                infoLayout.visibility = View.GONE
            }
        }
    }

    companion object {
        const val HIDE_DELAY_MILLIS = 1000L
    }
}
