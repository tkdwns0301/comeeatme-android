package com.hand.comeeatme.data.repository.restaurant

import com.hand.comeeatme.data.network.RestaurantService
import com.hand.comeeatme.data.response.restaurant.DetailRestaurantResponse
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantRepository(
    private val restaurantService: RestaurantService,
    private val ioDispatcher: CoroutineDispatcher,
) : RestaurantRepository {
    override suspend fun getSearchRestaurants(
        accessToken: String,
        page: Long?,
        size: Long?,
        sort: Boolean?,
        keyword: String,
    ): SimpleRestaurantResponse? = withContext(ioDispatcher) {
        val response = restaurantService.getSearchRestaurantList(
            Authorization = "Bearer $accessToken",
            page = page,
            size = size,
            sort = sort,
            keyword = keyword
        )

        if (response.isSuccessful) {
            response.body()!!
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