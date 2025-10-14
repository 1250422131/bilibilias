package com.imcys.bilibilias.ui.download.navigation

import android.os.Parcelable
import androidx.navigation3.runtime.NavKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class DownloadRoute(
    val defaultListIndex: Int = 0,
): NavKey, Parcelable

