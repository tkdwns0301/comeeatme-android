package com.hand.comeeatme.data.repository.restaurant

import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.network.RestaurantService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.response.restaurant.DetailRestaurantResponse
import com.hand.comeeatme.data.response.restaurant.RestaurantsRankResponse
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val oAuthService: OAuthService,
    private val restaurantService: RestaurantService,
    private val ioDispatcher: CoroutineDispatcher,
) : RestaurantRepository {
    override suspend fun getSearchRestaurants(
        accessToken: String,
        page: Long?,
        size: Long?,
        keyword: String,
    ): SimpleRestaurantResponse? = withContext(ioDispatcher) {
        val response = restaurantService.getSearchRestaurantList(
            Authorization = "Bearer $accessToken",
            page = page,
            size = size,
            keyword = keyword
        )

        if (response.isSuccessful) {
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}",
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getSearchRestaurants(
                    "${appPreferenceManager.getAccessToken()}",
                    page,
                    size,
                    keyword
                )
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun getRestaurantsRank(
        accessToken: String,
        page: Long?,
        size: Long?,
        addressCode: String,
        perImageNum: Long,
        sort: String,
    ): RestaurantsRankResponse? = withContext(ioDispatcher) {
        val response = restaurantService.getRestaurantsRank(
            Authorization = "Bearer $accessToken",
            page = page,
            size = size,
            addressCode = addressCode,
            perImageNum = perImageNum,
            sort = sort,
        )

        if (response.isSuccessful) {
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}",
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getRestaurantsRank(
                    "${appPreferenceManager.getAccessToken()}",
                    page,
                    size,
                    addressCode,
                    perImageNum,
                    sort
                )
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun getDetailRestaurant(
        accessToken: String,
        restaurantId: Long,
    ): DetailRestaurantResponse? = withContext(ioDispatcher) {
        val response = restaurantService.getDetailRestaurant(
            Authorization = "Bearer $accessToken",
            restaurantId = restaurantId
        )

        if (response.isSuccessful) {
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}",
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getDetailRestaurant(
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