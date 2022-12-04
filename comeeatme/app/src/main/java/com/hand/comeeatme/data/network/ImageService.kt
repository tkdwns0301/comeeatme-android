package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.image.ImageResponse
import com.hand.comeeatme.data.response.image.RestaurantImageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ImageService {
    // 처리된 이미지 저장 API
    @Multipart
    @POST("/v1/images/scaled")
    suspend fun sendImages(
        @Header("Authorization") Authorization: String,
        @Part images: List<MultipartBody.Part>?,
    ): Response<ImageResponse>

    // 음식점 이미지 조회
    @GET("/v1/restaurants/{restaurantId}/images")
    suspend fun getRestaurantImage(
        @Header("Authorization") Authorization: String,
        @Path("restaurantId") restaurantId: Long,
    ): Response<RestaurantImageResponse>
}