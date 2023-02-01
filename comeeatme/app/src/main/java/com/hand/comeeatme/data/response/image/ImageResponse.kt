package com.hand.comeeatme.data.response.image

data class ImageResponse(
    val success: Boolean,
    val data: ImageData
)

data class ImageData (
    val ids: List<Long>
        )
