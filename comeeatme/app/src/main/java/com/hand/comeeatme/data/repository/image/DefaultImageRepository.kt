package com.hand.comeeatme.data.repository.image

import com.hand.comeeatme.data.network.ImageService
import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.response.image.ImageResponse
import com.hand.comeeatme.data.response.image.RestaurantImageResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class DefaultImageRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val oAuthService: OAuthService,
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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                sendImages(
                    "${appPreferenceManager.getAccessToken()}",
                    images
                )
            } else {
                null
            }
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
            response.body()!!
        }
        else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getRestaurantImage(
                    "${appPreferenceManager.getAccessToken()}",
                    restaurantId
                )
            } else {
                null
            }
        } else {
            null
        }

    }
}