package com.imcys.bilibilias.core.http.downloader

import com.imcys.bilibilias.core.http.downloader.model.DownloadId
import com.imcys.bilibilias.core.http.downloader.model.DownloadOptions
import com.imcys.bilibilias.core.http.downloader.model.DownloadProgress
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import kotlinx.coroutines.flow.Flow

/**
 * Interface for downloading HTTP media files, including HLS (m3u8) streams and regular media files (.mp4, .mkv, etc.).
 *
 * This interface handles:
 * - Management of multiple concurrent downloads
 * - Progress reporting via Flow for all downloads
 * - Pause/resume/cancel functionality for individual downloads
 * - Support for both HLS streams and regular media files using HTTP range requests
 */
interface HttpDownloader : AutoCloseable {
    /**
     * Flow of progress updates for all downloads.
     */
    val progressFlow: Flow<DownloadProgress>

    /**
     * Gets a flow of progress updates for a specific download.
     */
    fun getProgressFlow(downloadId: DownloadId): Flow<DownloadProgress>

    /**
     * Flow that emits the entire list of known download states.
     * These states remain until removed internally (e.g. upon close).
     */
    val downloadStatesFlow: Flow<List<DownloadState>>

    /**
     * Initialize this downloader. Should be called before starting downloads.
     *
     * This may load any persisted download states, but does NOT resume downloads.
     * You may need to call [resume] for each download [getActiveDownloadIds] to resume them.
     */
    suspend fun init()

    /**
     * Starts a new download and returns its initial download state.
     *
     * @param parentDirectory absolute path
     */
    suspend fun download(
        url: String,
        options: DownloadOptions = DownloadOptions(),
    ): DownloadId

    /**
     * Starts a new download with a specific ID.
     *
     * @return initial download state if the download job is newly created,
     *  or the snapshot state of the download job if job with [downloadId] already exists.
     */
    suspend fun downloadWithId(
        downloadId: DownloadId,
        url: String,
        options: DownloadOptions = DownloadOptions(),
    ): DownloadState?

    /**
     * Resumes a previously paused or failed download by ID.
     */
    suspend fun resume(downloadId: DownloadId): Boolean

    /**
     * Gets all currently active download IDs.
     */
    suspend fun getActiveDownloadIds(): List<DownloadId>

    /**
     * Pauses a specific download.
     */
    suspend fun pause(downloadId: DownloadId): Boolean

    /**
     * Pauses all active downloads.
     */
    suspend fun pauseAll(): List<DownloadId>

    /**
     * Cancels a specific download.
     */
    suspend fun cancel(downloadId: DownloadId): Boolean

    /**
     * Cancels all active downloads.
     */
    suspend fun cancelAll()

    /**
     * Gets the current state of a download by ID.
     */
    suspend fun getState(downloadId: DownloadId): DownloadState?

    /**
     * Gets states of all known downloads.
     */
    suspend fun getAllStates(): List<DownloadState>

    /**
     * Closes and releases all resources used by this downloader.
     */
    override fun close()
}






