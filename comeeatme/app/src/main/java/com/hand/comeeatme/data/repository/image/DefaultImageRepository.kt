package com.hand.comeeatme.data.repository.image

import com.hand.comeeatme.data.network.ImageService
import com.hand.comeeatme.data.response.image.ImageResponse
import com.hand.comeeatme.data.response.image.RestaurantImageResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class DefaultImageRepository(
    private val imageService: ImageService,
    private val ioDispatcher: CoroutineDispatcher,
) : ImageRepository {
    override suspend fun sendImages(
        accessToken: String,
        images: ArrayList<MultipartBody.Part>?,
    ): ImageResponse? = withContext(ioDispatcher) {
        val response = imageService.sendImages(
            Authorization = "Bearer $accessToken",
            images = images
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun getRestaurantImage(
        accessToken: String,
        restaurantId: Long,
    ): RestaurantImageResponse? = withContext(ioDispatcher) {
        val response = imageService.getRestaurantImage(
            Authorization = "Bearer $accessToken",
            restaurantId = restaurantId
        )

        if (response.isSuccessful) {
            if (!response.body()!!.success) {
                null
            } else {
                response.body()!!
            }
        } else {
            null
        }

    }
}