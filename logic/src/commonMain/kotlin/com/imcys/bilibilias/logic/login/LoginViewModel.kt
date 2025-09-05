package com.imcys.bilibilias.logic.login

import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.core.logging.logger
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid


class LoginViewModel(
    val cookieStateMachine: CookieStateMachine,
    val qrCodeStateMachine: QrCodeLoginStateMachine,
    private val preferences: AsPreferencesDataSource,
    private val api: BilibiliApi,
    private val applicationScope: CoroutineScope,
) : ViewModel() {
    fun save() {
        applicationScope.launch {
            // Read an image file
            val originalImageFile = PlatformFile("/path/to/original.jpg")
            val imageBytes = originalImageFile.readBytes()

            // Compress the image
            val compressedBytes = FileKit.compressImage(
                bytes = imageBytes,
                quality = 100,
                maxWidth = 1024,
                maxHeight = 1024,
                imageFormat = ImageFormat.PNG
            )

            // Save the compressed image
            val compressedFile = PlatformFile(FileKit.filesDir, "compressed.jpg")
            compressedFile.write(compressedBytes)
        }
    }

    override fun onCleared() {
        applicationScope.launch {
            val data = api.getNavigationData()
            preferences.setSelfInfo(
                SelfInfo(
                    Uuid.random(),
                    data.mid!!,
                    data.uname!!,
                    data.face!!
                )
            )
        }
    }
    companion object {
        private val logger = logger<LoginViewModel>()
    }
}