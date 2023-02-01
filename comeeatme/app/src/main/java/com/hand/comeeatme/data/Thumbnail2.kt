package com.hand.comeeatme.data

import android.net.Uri

data class Thumbnail2(
    val id: Long,
    val position: Int,
    val uri: Uri,
    val path: String,
    var isChecked: Boolean = false
)

