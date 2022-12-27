package com.hand.comeeatme.data.repository.favorite

import com.hand.comeeatme.data.network.FavoriteService
import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.response.favorite.FavoritePostResponse
import com.hand.comeeatme.data.response.like.SuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultFavoriteRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val oAuthService: OAuthService,
    private val favoriteService: FavoriteService,
    private val ioDispatcher: CoroutineDispatcher,
) : FavoriteRepository {
    override suspend fun favoriteRestaurant(accessToken: String, restaurantId: Long): SuccessResponse? =
        withContext(ioDispatcher) {
            val response = favoriteService.favoriteRestaurant(
                Authorization = "Bearer $accessToken",
                restaurantId = restaurantId
            )

            if (response.isSuccessful) {
                response.body()!!
            }  else if (response.code() == 401) {
                val response2 = oAuthService.reissueToken(
                    "Bearer ${appPreferenceManager.getRefreshToken()}"
                )

                if (response2.isSuccessful) {
                    appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                    appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                    favoriteRestaurant(
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

    override suspend fun unFavoriteRestaurant(accessToken: String, restaurantId: Long): SuccessResponse? = withContext(ioDispatcher){
        val response = favoriteService.unFavoriteRestaurant(
            Authorization = "Bearer $accessToken",
            restaurantId = restaurantId
        )

        if(response.isSuccessful) {
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                unFavoriteRestaurant(
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

    override suspend fun getAllFavorite(
        accessToken: String,
        memberId: Long,
        page: Long?,
        size: Long?,
    ): FavoritePostResponse?  = withContext(ioDispatcher) {
        val response = favoriteService.getAllFavorite(
            Authorization = "Bearer $accessToken",
            memberId = memberId,
            page = page!!,
            size = size!!,
            perImageNum = 3
        )

        if(response.isSuccessful) {
            response.body()!!
        }else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getAllFavorite(
                    "${appPreferenceManager.getAccessToken()}",
                    memberId,
                    page,
                    size
                )
            } else {
                null
            }
        }  else {
            null
        }
    }
}