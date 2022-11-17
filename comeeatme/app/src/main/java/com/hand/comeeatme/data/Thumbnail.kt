package com.hand.comeeatme.data

import android.graphics.Bitmap
import android.net.Uri

data class Thumbnail(
    val position: Int,
    val thumbnail: Bitmap,
    val uri: Uri,
    var path: String,
    var compressPath: String?,
    var isChecked: Boolean,
)

