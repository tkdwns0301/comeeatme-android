package com.hand.comeeatme.data

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SampleImage (
    val position: Int,
    val thumbnail: Bitmap,
    val image: Uri,
    var path: String?
) : Parcelable

@Parcelize
class Position (
    var position : Int,
    var path : String,
) : Parcelable

