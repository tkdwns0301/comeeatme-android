package com.hand.comeeatme.data.repository.image

import com.hand.comeeatme.data.response.image.ImageResponse
import com.hand.comeeatme.data.response.image.RestaurantImageResponse
import okhttp3.MultipartBody

interface ImageRepository {
    suspend fun sendImages(accessToken: String, images: ArrayList<MultipartBody.Part>?): ImageResponse?
    suspend fun getRestaurantImage(accessToken: String, restaurantId: Long): RestaurantImageResponse?
}