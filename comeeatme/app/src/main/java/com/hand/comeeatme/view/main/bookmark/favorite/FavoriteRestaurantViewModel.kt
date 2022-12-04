package com.hand.comeeatme.view.main.bookmark.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.favorite.FavoriteRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class FavoriteRestaurantViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val favoriteRepository: FavoriteRepository,
) : BaseViewModel() {
    val favoritePostStateLiveData =
        MutableLiveData<FavoriteRestaurantState>(FavoriteRestaurantState.Uninitialized)

    fun favoriteRestaurant(
        restaurantId: Long,
    ) = viewModelScope.launch {
        val response = favoriteRepository.favoriteRestaurant("${appPreferenceManager.getAccessToken()}",
            restaurantId)

        response?.let {
            favoritePostStateLiveData.value = FavoriteRestaurantState.FavoritePostSuccess
        } ?: run {
            favoritePostStateLiveData.value = FavoriteRestaurantState.Error(
                "즐겨찾기 실패"
            )
        }
    }

    fun unFavoriteRestaurant(
        restaurantId: Long,
    ) = viewModelScope.launch {
        val response = favoriteRepository.unFavoriteRestaurant("${appPreferenceManager.getAccessToken()}",
            restaurantId)

        response?.let {
            favoritePostStateLiveData.value = FavoriteRestaurantState.FavoritePostSuccess
        } ?: run {
            favoritePostStateLiveData.value = FavoriteRestaurantState.Error(
                "즐겨찾기 취소 실패"
            )
        }
    }

    fun getAllFavorite(
        page: Long?,
        size: Long?,
        sort: Boolean?,
    ) = viewModelScope.launch {
        favoritePostStateLiveData.value = FavoriteRestaurantState.Loading

        val response = favoriteRepository.getAllFavorite(
            "${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId(),
            page,
            size,
            sort,
        )

        response?.let {
            if (it.data.content.isEmpty()) {
                favoritePostStateLiveData.value = FavoriteRestaurantState.Error(
                    "즐겨찾기 목록이 없어요,,,"
                )
            } else {
                favoritePostStateLiveData.value = FavoriteRestaurantState.Success(
                    response = it
                )
            }
        } ?: run {
            favoritePostStateLiveData.value = FavoriteRestaurantState.Error(
                "즐겨찾기 목록을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

}