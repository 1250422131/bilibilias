package com.imcys.bilibilias.util

import android.util.Log
import androidx.profileinstaller.ProfileVerifier
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileVerifierLogger @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
) {
    companion object {
        private const val TAG = "ProfileInstaller"
    }

    operator fun invoke() = scope.launch {
        val status = ProfileVerifier.getCompilationStatusAsync().get()
        Log.d(TAG, "Status code: ${status.profileInstallResultCode}")
        Log.d(
            TAG,
            when {
                status.isCompiledWithProfile -> "App compiled with profile"
                status.hasProfileEnqueuedForCompilation() -> "Profile enqueued for compilation"
                else -> "Profile not compiled nor enqueued"
            },
        )
    }
}
