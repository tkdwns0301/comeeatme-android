package com.hand.comeeatme.data.repository.restaurant

import com.hand.comeeatme.data.network.RestaurantService
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
        name: String,
    ): SimpleRestaurantResponse? = withContext(ioDispatcher) {
        val response = restaurantService.getSearchRestaurantList(
            Authorization = "Bearer $accessToken",
            page = page,
            size = size,
            sort = sort,
            name = name
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

}