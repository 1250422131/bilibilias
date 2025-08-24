package com.imcys.bilibilias.logic.login

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.logic.root.AppComponentContext
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

interface LoginComponent {
    val cookieStateMachine: CookieStateMachine
    val qrCodeStateMachine: QrCodeLoginStateMachine
    fun save()
}

class DefaultLoginComponent(
    componentContext: AppComponentContext,
    override val cookieStateMachine: CookieStateMachine,
    override val qrCodeStateMachine: QrCodeLoginStateMachine,
    private val preferences: AsPreferencesDataSource,
) : LoginComponent, AppComponentContext by componentContext {

    init {
        lifecycle.doOnDestroy {
            applicationScope.launch {
                val data = BilibiliApi.getNavigationData()
                preferences.setSelfInfo(
                    SelfInfo(
                        Uuid.random(),
                        data.mid,
                        data.uname,
                        data.face
                    )
                )
            }
        }
    }

    override fun save() {
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
    companion object {
        private val logger = logger<LoginComponent>()
    }
}