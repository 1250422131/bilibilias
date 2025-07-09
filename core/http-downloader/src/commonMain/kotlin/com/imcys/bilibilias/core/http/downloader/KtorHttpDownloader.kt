package com.imcys.bilibilias.core.http.downloader

import co.touchlab.kermit.Logger
import com.imcys.bilibilias.core.http.downloader.model.DownloadError
import com.imcys.bilibilias.core.http.downloader.model.DownloadErrorCode
import com.imcys.bilibilias.core.http.downloader.model.DownloadId
import com.imcys.bilibilias.core.http.downloader.model.DownloadOptions
import com.imcys.bilibilias.core.http.downloader.model.DownloadProgress
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus.CANCELED
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus.COMPLETED
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus.FAILED
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus.PAUSED
import com.imcys.bilibilias.core.http.downloader.model.SegmentInfo
import com.imcys.bilibilias.core.io.absolutePath
import com.imcys.bilibilias.core.io.inSystem
import com.imcys.bilibilias.core.io.resolve
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.appendAll
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.asSource
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * A simple implementation of [HttpDownloader] that uses Ktor and coroutines.
 * Supports both M3u8 streams and regular media files.
 */
@OptIn(ExperimentalTime::class)
open class KtorHttpDownloader(
    protected val client: HttpClient,
    private val fileSystem: FileSystem,
    private val baseSaveDir: Path,
    computeDispatcher: CoroutineContext = Dispatchers.Default,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO,
    val clock: Clock = Clock.System,
) : HttpDownloader {

    protected val scope = CoroutineScope(SupervisorJob() + computeDispatcher)

    private val _progressFlow = MutableSharedFlow<DownloadProgress>(
        replay = 1,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val progressFlow: Flow<DownloadProgress> = _progressFlow.asSharedFlow()

    override fun getProgressFlow(downloadId: DownloadId): Flow<DownloadProgress> {
        return progressFlow.filter { it.downloadId == downloadId }.onStart {
            emit(createProgress(getState(downloadId) ?: return@onStart))
        }
    }

    /**
     * Our map of download states.
     */
    protected val _downloadStatesFlow = MutableStateFlow(emptyMap<DownloadId, DownloadEntry>())

    override val downloadStatesFlow: Flow<List<DownloadState>> =
        _downloadStatesFlow.map { it.values.map { entry -> entry.state } }

    override suspend fun init() {
        // No initialization needed, but place for potential future logic
        logger.i { "KtorHttpDownloader initialized." }
    }

    protected val stateMutex = Mutex()

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun download(
        url: String,
        options: DownloadOptions,
    ): DownloadId {
        val downloadId = DownloadId(value = Uuid.toString())
        return downloadWithId(downloadId, url, options)?.downloadId ?: downloadId
    }

    protected fun getMediaTypeFromUrl(url: String): MediaType? {
        val parsed = io.ktor.http.Url(url)
        val path = parsed.encodedPath // without query
        fun guessFromValue(value: String): MediaType? {
            return when {
                value.endsWith(".m3u8", ignoreCase = true) -> MediaType.M3U8
                value.endsWith(".mp4", ignoreCase = true) -> MediaType.MP4
                value.endsWith(".mkv", ignoreCase = true) -> MediaType.MKV
                else -> null
            }
        }
        return guessFromValue(path) // https://foo.com/bar.m3u8
            ?: guessFromValue(url) // https://foo.com/index.php?key=video.m3u8
    }

    override suspend fun downloadWithId(
        downloadId: DownloadId,
        url: String,
        options: DownloadOptions,
    ): DownloadState? {
        val mediaType = getMediaTypeFromUrl(url) ?: MediaType.MP4
        logger.i { "Preparing to download with id=$downloadId, url=$url, mediaType=$mediaType" }

        // 1) Set initial state if not present
        stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value.toMutableMap()
            val existingEntry = currentMap[downloadId]
            if (existingEntry != null) {
                // If there's already a state in COMPLETED, do nothing
                logger.i { "Existing completed download found for $downloadId, ignoring." }
                return existingEntry.state
            }
            val segmentCacheDir = ("segments_" + downloadId.value)
                .also { fileSystem.createDirectories(baseSaveDir.resolve(it)) }
            val initialState = DownloadState(
                downloadId = downloadId,
                url = url,
                relativeOutputPath = downloadId.value,
                segments = emptyList(),
                totalSegments = 0,
                downloadedBytes = 0L,
                timestamp = clock.now().toEpochMilliseconds(),
                status = DownloadStatus.INITIALIZING,
                relativeSegmentCacheDir = segmentCacheDir,
                mediaType = mediaType,
            )
            currentMap[downloadId] = DownloadEntry(job = null, state = initialState)
            _downloadStatesFlow.value = currentMap
            logger.i { "Created initial state for $downloadId" }
        }
        emitProgress(downloadId)

        // 2) Create segments
        if (!createSegments(downloadId, url, mediaType, options)) {
            // If creation failed, the state is set to FAILED. We stop here.
            return null
        }

        // 3) Mark status=DOWNLOADING and launch the job
        updateState(downloadId) {
            it.copy(status = DownloadStatus.DOWNLOADING)
        }
        emitProgress(downloadId)

        logger.i { "Launching download job for $downloadId" }
        val job = scope.launch(start = CoroutineStart.UNDISPATCHED) {
            try {
                logger.i { "Downloading segments for $downloadId" }
                downloadSegments(downloadId, options)

                updateState(downloadId) {
                    it.copy(
                        status = DownloadStatus.MERGING,
                        timestamp = clock.now().toEpochMilliseconds()
                    )
                }
                emitProgress(downloadId)
                logger.i { "Merging segments for $downloadId" }

                mergeSegments(downloadId)

                updateState(downloadId) {
                    it.copy(status = COMPLETED, timestamp = clock.now().toEpochMilliseconds())
                }
                emitProgress(downloadId)
                logger.i { "Download completed for $downloadId" }
            } catch (e: CancellationException) {
                logger.i { "Download cancelled for $downloadId" }
                throw e
            } catch (e: Throwable) {
                logger.i { "Download failed for $downloadId: ${e.message}" }
                updateState(downloadId) {
                    it.copy(
                        status = FAILED,
                        error = DownloadError(
                            code = DownloadErrorCode.UNEXPECTED_ERROR,
                            technicalMessage = e.message,
                        ),
                        timestamp = clock.now().toEpochMilliseconds(),
                    )
                }
                emitProgress(downloadId)
            }
        }

        // 4) Store the job
        return stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value.toMutableMap()
            val entry = currentMap[downloadId]
                ?: error("Job of new download request $downloadId is created, but the download state is not found.")
            currentMap[downloadId] = entry.copy(job = job)
            _downloadStatesFlow.value = currentMap

            entry.state
        }
    }

    /**
     * If segments are empty, tries to create them again.
     * Returns false if creation fails (the state will be marked FAILED), otherwise true.
     */
    override suspend fun resume(downloadId: DownloadId): Boolean {
        val st = getState(downloadId) ?: return false
        if (st.status != PAUSED && st.status != FAILED) {
            if (st.status != COMPLETED) {
                logger.i { "Cannot resume $downloadId because status=${st.status}" }
            }
            return false
        }

        // Check if there's already an active job
        val alreadyActive = stateMutex.withLock {
            val existingEntry = _downloadStatesFlow.value[downloadId]
            existingEntry?.job?.isActive == true
        }
        if (alreadyActive) {
            logger.i { "Attempting to resume $downloadId but there's already an active job." }
            emitProgress(downloadId)
            return true
        }

        logger.i { "Resuming $downloadId with status=${st.status}" }

        // If we have no segments, it means we failed during segment creation
        if (st.segments.isEmpty()) {
            if (!createSegments(downloadId, st.url, st.mediaType, DownloadOptions())) {
                return false // Already marked as FAILED
            }
        }

        // Mark status = DOWNLOADING
        updateState(downloadId) {
            it.copy(status = DownloadStatus.DOWNLOADING)
        }
        emitProgress(downloadId)

        val job = scope.launch(start = CoroutineStart.UNDISPATCHED) {
            try {
                logger.i { "Resumed: downloading segments for $downloadId" }
                downloadSegments(downloadId, DownloadOptions())

                updateState(downloadId) {
                    it.copy(status = DownloadStatus.MERGING)
                }
                emitProgress(downloadId)
                logger.i { "Merging segments for resumed $downloadId" }
                mergeSegments(downloadId)

                updateState(downloadId) {
                    it.copy(status = COMPLETED)
                }
                emitProgress(downloadId)
                logger.i { "Download completed after resume for $downloadId" }
            } catch (e: CancellationException) {
                logger.i { "Download cancelled while resumed for $downloadId" }
                throw e
            } catch (t: Throwable) {
                logger.i { "Resumed download failed for $downloadId: ${t.message}" }
                updateState(downloadId) {
                    it.copy(
                        status = FAILED,
                        error = DownloadError(
                            DownloadErrorCode.UNEXPECTED_ERROR,
                            technicalMessage = t.message
                        ),
                    )
                }
                emitProgress(downloadId)
            }
        }

        // Store the new job
        stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value.toMutableMap()
            val entry = currentMap[downloadId] ?: return@withLock
            currentMap[downloadId] = entry.copy(job = job)
            _downloadStatesFlow.value = currentMap
        }
        return true
    }

    override suspend fun getActiveDownloadIds(): List<DownloadId> {
        return stateMutex.withLock {
            _downloadStatesFlow.value.values
                .filter { it.state.status == DownloadStatus.DOWNLOADING || it.state.status == DownloadStatus.INITIALIZING }
                .map { it.state.downloadId }
        }
    }

    override suspend fun pause(downloadId: DownloadId): Boolean {
        stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value.toMutableMap()
            val entry = currentMap[downloadId] ?: return false
            val job = entry.job
            if (job != null) {
                if (!job.isActive) {
                    logger.i { "Cannot pause $downloadId because job is not active." }
                    return false
                }
                logger.i { "Pausing download $downloadId" }
                job.cancel()
            }

            val oldState = entry.state
            currentMap[downloadId] = entry.copy(
                job = null,
                state = oldState.copy(status = PAUSED),
            )
            _downloadStatesFlow.value = currentMap
        }
        emitProgress(downloadId)
        return true
    }

    override suspend fun pauseAll(): List<DownloadId> {
        val paused = mutableListOf<DownloadId>()
        stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value.toMutableMap()
            currentMap.forEach { (id, entry) ->
                val job = entry.job
                if (job != null && job.isActive) {
                    logger.i { "Pausing download $id" }
                    job.cancel()
                    currentMap[id] = entry.copy(
                        job = null,
                        state = entry.state.copy(status = PAUSED),
                    )
                    paused.add(id)
                }
            }
            _downloadStatesFlow.value = currentMap
        }
        paused.forEach { emitProgress(it) }
        return paused
    }

    override suspend fun cancel(downloadId: DownloadId): Boolean {
        stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value.toMutableMap()
            val entry = currentMap[downloadId] ?: return false
            val job = entry.job
            if (job != null && job.isActive) {
                logger.i { "Cancelling download $downloadId" }
                job.cancel()
            }
            currentMap[downloadId] = entry.copy(
                job = null,
                state = entry.state.copy(status = CANCELED),
            )
            _downloadStatesFlow.value = currentMap
        }
        emitProgress(downloadId)
        return true
    }

    override suspend fun cancelAll() {
        logger.i { "Cancelling all downloads." }
        stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value.toMutableMap()
            currentMap.forEach { (id, entry) ->
                if (entry.job?.isActive == true) {
                    logger.i { "Cancelling download $id" }
                    entry.job.cancel()
                }
                val st = entry.state
                if (st.status in listOf(
                        DownloadStatus.INITIALIZING,
                        DownloadStatus.DOWNLOADING, PAUSED, DownloadStatus.MERGING
                    )
                ) {
                    currentMap[id] = entry.copy(
                        job = null,
                        state = st.copy(status = CANCELED),
                    )
                }
            }
            _downloadStatesFlow.value = currentMap
        }
        val allIds = stateMutex.withLock { _downloadStatesFlow.value.keys.toList() }
        allIds.forEach { emitProgress(it) }
    }

    override suspend fun getState(downloadId: DownloadId): DownloadState? {
        return stateMutex.withLock {
            _downloadStatesFlow.value[downloadId]?.state
        }
    }

    override suspend fun getAllStates(): List<DownloadState> {
        return stateMutex.withLock {
            _downloadStatesFlow.value.values.map { it.state }
        }
    }

    override fun close() {
        logger.i { "Closing KtorHttpDownloader." }
        scope.launch(NonCancellable + CoroutineName("M3u8Downloader.close")) {
            closeSuspend()
        }
    }

    suspend fun closeSuspend() {
        logger.i { "closeSuspend() called; joining all active jobs." }
        stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value
            currentMap.forEach { (_, entry) ->
                if (entry.job?.isActive == true) {
                    entry.job.cancelAndJoin()
                }
            }
            _downloadStatesFlow.value = emptyMap()
        }
        scope.cancel()
        logger.i { "KtorHttpDownloader closed." }
    }

    // -------------------------------------------------------
    // Internal details
    // -------------------------------------------------------

    protected data class DownloadEntry(val job: Job?, val state: DownloadState)

    /**
     * Create and return the directory in which segment files will be stored.
     */
    protected suspend fun createSegmentCacheDir(
        outputPath: Path,
        downloadId: DownloadId,
    ): Path = withContext(ioDispatcher) {
        val cacheDirName = outputPath.name + "_segments_" + downloadId.value
        val parentDir = outputPath.parent ?: Path(".")
        val cacheDir = parentDir.resolve(cacheDirName)
        fileSystem.createDirectories(cacheDir)
        logger.i { "Created segment cache dir for $downloadId at $cacheDir" }
        cacheDir
    }

    /**
     * Helper that attempts to create segments for the given [downloadId] & [url].
     * Returns `true` if it succeeds, or `false` if it fails (the state is set to FAILED).
     */
    private suspend fun createSegments(
        downloadId: DownloadId,
        url: String,
        mediaType: MediaType,
        options: DownloadOptions,
    ): Boolean {
        try {
            val newSegments = when (mediaType) {
                MediaType.M3U8 -> {
                    emptyList()
                }

                MediaType.MP4, MediaType.MKV -> {
                    logger.i { "Creating range segments for $downloadId" }
                    createRangeSegments(downloadId, url, options)
                }
            }

            updateState(downloadId) {
                it.copy(
                    segments = newSegments,
                    totalSegments = newSegments.size,
                )
            }
            emitProgress(downloadId)
            logger.i { "Created ${newSegments.size} segments for $downloadId" }
            return true
        } catch (e: Throwable) {
            handleSegmentCreationFailure(downloadId, e)
            return false
        }
    }

    /**
     * If segment creation fails (404, parse error, OOM, etc.), mark the state as FAILED and emit progress.
     */
    private suspend fun handleSegmentCreationFailure(downloadId: DownloadId, e: Throwable) {
        logger.e(e) { "Segment creation failed for $downloadId: ${e.message}" }
        updateState(downloadId) {
            it.copy(
                status = FAILED,
                error = DownloadError(
                    code = DownloadErrorCode.UNEXPECTED_ERROR,
                    technicalMessage = e.message,
                ),
                timestamp = clock.now().toEpochMilliseconds(),
            )
        }
        emitProgress(downloadId)
    }

    protected suspend fun updateState(
        downloadId: DownloadId,
        transform: (DownloadState) -> DownloadState
    ) {
        stateMutex.withLock {
            val currentMap = _downloadStatesFlow.value.toMutableMap()
            val entry = currentMap[downloadId] ?: return
            val oldState = entry.state
            val newState = transform(oldState)
            currentMap[downloadId] = entry.copy(state = newState)
            _downloadStatesFlow.value = currentMap
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    protected suspend fun createRangeSegments(
        downloadId: DownloadId,
        url: String,
        options: DownloadOptions,
    ): List<SegmentInfo> {
        val cacheDir = baseSaveDir.resolve(
            getState(downloadId)?.relativeSegmentCacheDir ?: error("Cache dir not found"),
        )

        val rangeProbe = probeRangeSupport(url, options)

        // If the probe fails or the server doesn't support range => single segment
        val (contentLength, rangeSupported) = rangeProbe
            ?: return listOf(
                SegmentInfo(
                    index = 0,
                    url = url,
                    isDownloaded = false,
                    byteSize = -1,
                    relativeTempFilePath = cacheDir.resolve("0.part").inSystem.absolutePath
                        .substringAfter(baseSaveDir.inSystem.absolutePath),
                    rangeStart = null,
                    rangeEnd = null,
                ),
            )

        if (!rangeSupported) {
            logger.i { "Range not supported for $downloadId, creating single segment." }
            return listOf(
                SegmentInfo(
                    index = 0,
                    url = url,
                    isDownloaded = false,
                    byteSize = contentLength, // might be -1 if unknown
                    relativeTempFilePath = cacheDir.resolve("0.part").inSystem.absolutePath
                        .substringAfter(baseSaveDir.inSystem.absolutePath),
                    rangeStart = null,
                    rangeEnd = null,
                ),
            )
        }

        logger.i { "Range supported for $downloadId, total file size: $contentLength" }
        val segmentSize = 5 * 1024 * 1024L // 5MB
        if (contentLength <= segmentSize) {
            logger.i { "File is smaller than $segmentSize for $downloadId, single segment." }
            return listOf(
                SegmentInfo(
                    index = 0,
                    url = url,
                    isDownloaded = false,
                    byteSize = contentLength,
                    relativeTempFilePath = cacheDir.resolve("0.part").inSystem.absolutePath
                        .substringAfter(baseSaveDir.inSystem.absolutePath),
                    rangeStart = 0,
                    rangeEnd = contentLength - 1,
                ),
            )
        }

        logger.i { "Splitting file into segments of size=$segmentSize for $downloadId" }
        val segments = mutableListOf<SegmentInfo>()
        var start = 0L
        var index = 0
        while (start < contentLength) {
            val end = (start + segmentSize - 1).coerceAtMost(contentLength - 1)
            segments.add(
                SegmentInfo(
                    index = index,
                    url = url,
                    isDownloaded = false,
                    byteSize = (end - start + 1),
                    relativeTempFilePath = cacheDir.resolve("$index.part").inSystem.absolutePath
                        .substringAfter(baseSaveDir.inSystem.absolutePath),
                    rangeStart = start,
                    rangeEnd = end,
                ),
            )
            start = end + 1
            index++
        }

        logger.i { "Created ${segments.size} range segments for $downloadId" }
        return segments
    }

    /**
     * Attempt a small GET with Range=0-0 to detect whether partial content is supported
     * and to parse the total file size from 'Content-Range: bytes 0-0/<size>'.
     */
    private suspend fun probeRangeSupport(
        url: String,
        options: DownloadOptions,
    ): Pair<Long, Boolean>? {
        val rangeOptions = options.copy(
            headers = options.headers + ("Range" to "bytes=0-0"),
        )
        return try {
            client.prepareGet(url) {
                headers.appendAll(rangeOptions.headers)
            }.execute { response ->
                when (response.status.value) {
                    206 -> {
                        val contentRange =
                            response.headers[io.ktor.http.HttpHeaders.ContentRange]
                                ?: return@execute null
                        val totalSize = contentRange.substringAfter('/').toLongOrNull()
                            ?: return@execute null
                        totalSize to true // (size, range supported)
                    }

                    200 -> {
                        val length =
                            response.headers[io.ktor.http.HttpHeaders.ContentLength]?.toLongOrNull()
                                ?: -1L
                        length to false
                    }

                    else -> null
                }
            }
        } catch (_: Throwable) {
            null
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    protected suspend fun downloadSingleSegment(
        segmentInfo: SegmentInfo,
        options: DownloadOptions,
    ): Long {
        // If we have a range, add it to the request headers.
        val finalOptions = if (segmentInfo.rangeStart != null && segmentInfo.rangeEnd != null) {
            options.copy(
                headers = options.headers + ("Range" to "bytes=${segmentInfo.rangeStart}-${segmentInfo.rangeEnd}"),
            )
        } else {
            options
        }
        logger.i { "Downloading segment index=${segmentInfo.index}, range=(${segmentInfo.rangeStart}-${segmentInfo.rangeEnd})" }

        return httpGet(segmentInfo.url, finalOptions) { statement ->
            val response = statement.execute()
            val channel = response.bodyAsChannel()

            val segmentPath = baseSaveDir.resolve(segmentInfo.relativeTempFilePath)
            withContext(ioDispatcher) {
                fileSystem.createDirectories(
                    segmentPath.parent
                        ?: error("Parent dir not found for segmentInfo: $segmentInfo"),
                )
            }

            copyChannelToFile(channel, segmentPath).also {
                logger.i { "Segment index=${segmentInfo.index} downloaded, size=$it" }
            }
        }
    }

    /**
     * Reads from [channel] and writes to [filePath], returning the total bytes downloaded.
     */
    @OptIn(ExperimentalAtomicApi::class)
    private suspend fun copyChannelToFile(
        channel: ByteReadChannel,
        filePath: Path,
    ): Long {
        val totalBytes = AtomicLong(0L)
        fileSystem.sink(filePath).buffered().use { sink ->
            withContext(ioDispatcher) {
                totalBytes.fetchAndAdd(sink.transferFrom(channel.asSource()))
            }
        }
        return totalBytes.load()
    }

    @OptIn(DelicateCoroutinesApi::class, ExperimentalAtomicApi::class)
    protected suspend fun downloadSegments(downloadId: DownloadId, options: DownloadOptions) {
        val snapshot = getState(downloadId) ?: return
        if (snapshot.segments.isEmpty()) {
            logger.i { "No segments to download for $downloadId" }
            return
        }
        logger.i { "Downloading ${snapshot.segments.size} segments for $downloadId with concurrency=${options.maxConcurrentSegments}" }
        val semaphore = Semaphore(options.maxConcurrentSegments)

        coroutineScope {
            snapshot.segments.forEach { seg ->
                if (seg.isDownloaded) return@forEach
                semaphore.acquire()
                launch(ioDispatcher, start = CoroutineStart.ATOMIC) {
                    try {
                        // Retry with exponential backoff if download fails
                        val newSize = withRetry(
                            maxRetries = options.maxRetriesPerSegment,
                            baseDelayMillis = options.baseRetryDelayMillis,
                        ) {
                            downloadSingleSegment(seg, options)
                        }
                        markSegmentDownloaded(downloadId, seg.index, newSize)
                    } finally {
                        semaphore.release()
                    }
                }
            }
        }
        logger.i { "All segments downloaded for $downloadId" }
    }

    protected suspend fun markSegmentDownloaded(
        downloadId: DownloadId,
        segmentIndex: Int,
        byteSize: Long
    ) {
        logger.i { "Segment index=$segmentIndex fully downloaded for $downloadId, size=$byteSize" }
        updateState(downloadId) { old ->
            val updatedSegments = old.segments.map {
                if (it.index == segmentIndex) it.copy(
                    isDownloaded = true,
                    byteSize = byteSize
                ) else it
            }
            old.copy(downloadedBytes = old.downloadedBytes + byteSize, segments = updatedSegments)
        }
        emitProgress(downloadId)
    }

    protected suspend fun mergeSegments(downloadId: DownloadId) = withContext(ioDispatcher) {
        val st = getState(downloadId) ?: return@withContext
        val cacheDir = baseSaveDir.resolve(st.relativeSegmentCacheDir)
        val finalOutput = baseSaveDir.resolve(st.relativeOutputPath)

        fileSystem.sink(finalOutput).buffered().use { out ->
            st.segments.sortedBy { it.index }.forEach { seg ->
                fileSystem.source(baseSaveDir.resolve(seg.relativeTempFilePath)).buffered()
                    .use { input ->
                        input.transferTo(out)
                    }
            }
        }

        // remove segment files
        st.segments.forEach { seg ->
            fileSystem.delete(baseSaveDir.resolve(seg.relativeTempFilePath))
        }
        // remove the cache dir
        fileSystem.delete(cacheDir)
        logger.i { "Segments merged into $finalOutput, removed cache dir=$cacheDir for $downloadId" }
    }

    protected suspend fun emitProgress(downloadId: DownloadId) {
        val st = getState(downloadId) ?: return
        val progress = createProgress(st)
        _progressFlow.emit(progress)
    }

    private fun createProgress(st: DownloadState): DownloadProgress {
        val downloadedSegments = st.segments.count { it.isDownloaded }
        return DownloadProgress(
            downloadId = st.downloadId,
            url = st.url,
            totalSegments = st.totalSegments,
            downloadedSegments = downloadedSegments,
            downloadedBytes = st.downloadedBytes,
            totalBytes = st.segments.sumOf { it.byteSize.coerceAtLeast(0) }
                .coerceAtLeast(st.downloadedBytes),
            status = st.status,
            error = st.error,
        )
    }

    protected suspend inline fun <R> httpGet(
        url: String,
        options: DownloadOptions,
        block: (HttpStatement) -> R
    ): R {
        return block(client.prepareGet(url) { headers.appendAll(options.headers) })
    }

    suspend fun joinDownload(downloadId: DownloadId) {
        val job = stateMutex.withLock {
            _downloadStatesFlow.value[downloadId]?.job
        }
        if (job != null) {
            logger.i { "Waiting for download job to complete for $downloadId" }
            job.join()
            logger.i { "Download job completed for $downloadId" }
        }
    }

    /**
     * Helper to retry the [block] up to [maxRetries] times with exponential backoff.
     * The backoff delay starts at [baseDelayMillis] and doubles each time.
     */
    private suspend fun <T> withRetry(
        maxRetries: Int,
        baseDelayMillis: Long,
        block: suspend () -> T
    ): T {
        var attempt = 1
        var currentDelay = baseDelayMillis
        while (true) {
            try {
                return block()
            } catch (ce: CancellationException) {
                // Always rethrow cancellation
                throw ce
            } catch (ex: Throwable) {
                if (attempt >= maxRetries) {
                    logger.i {
                        "Segment download failed after $attempt/$maxRetries attempts; no more retries. " +
                                "Error: ${ex.message}"
                    }
                    throw ex
                }
                logger.i {
                    "Segment download failed on attempt $attempt/$maxRetries: ${ex.message}. " +
                            "Retrying after ${currentDelay}ms..."
                }
                delay(currentDelay)
                currentDelay = (currentDelay * 2).coerceAtMost(30_000L) // cap at 30s
                attempt++
            }
        }
    }

    private companion object {
        val logger = Logger.withTag("KtorHttpDownloader")
    }
}