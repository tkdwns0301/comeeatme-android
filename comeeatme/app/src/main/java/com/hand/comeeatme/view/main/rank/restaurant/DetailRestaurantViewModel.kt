package com.hand.comeeatme.view.main.rank.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.image.ImageRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class DetailRestaurantViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val restaurantRepository: RestaurantRepository,
    private val imageRepository: ImageRepository,
): BaseViewModel() {

    val detailRestaurantStateLiveData = MutableLiveData<DetailRestaurantState>(DetailRestaurantState.Uninitialized)

    fun getRestaurantImage(restaurantId: Long) = viewModelScope.launch {
        detailRestaurantStateLiveData.value = DetailRestaurantState.Loading

        val response = imageRepository.getRestaurantImage(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {
            detailRestaurantStateLiveData.value = DetailRestaurantState.ImageSuccess(
                response = it
            )
        }?:run {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Error(
                "음식점 사진을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun getDetailRestaurant(restaurantId: Long) = viewModelScope.launch {
        detailRestaurantStateLiveData.value = DetailRestaurantState.Loading

        val response = restaurantRepository.getDetailRestaurant(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Success(
                response = it
            )
        }?:run {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Error(
                "음식점 정보를 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

}