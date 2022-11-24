package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.image.ImageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ImageService {
    @Multipart
    @POST("/v1/images/scaled")
    suspend fun sendImages(
        @Header("Authorization") Authorization: String,
        @Part images: List<MultipartBody.Part>?,
    ): Response<ImageResponse>

}