package com.hand.comeeatme.view.main.bookmark.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.favorite.FavoriteRepository
import com.hand.comeeatme.data.response.favorite.FavoritePostContent
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class FavoriteRestaurantViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val favoriteRepository: FavoriteRepository,
) : BaseViewModel() {
    val favoritePostStateLiveData =
        MutableLiveData<FavoriteRestaurantState>(FavoriteRestaurantState.Uninitialized)

    private var page: Long = 0
    private var isLast: Boolean = false
    private var contents = arrayListOf<FavoritePostContent>()

    fun getIsLast() : Boolean = isLast
    fun setIsLast(isLast: Boolean) {
        this.isLast = isLast
    }

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
        isRefresh: Boolean,
    ) = viewModelScope.launch {
        favoritePostStateLiveData.value = FavoriteRestaurantState.Loading

        if(isRefresh) {
            page = 0
            contents = arrayListOf()
        }

        val response = favoriteRepository.getAllFavorite(
            "${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId(),
            page++,
            10,
        )

        response?.let {
            if (it.data.content.isNotEmpty()) {
                contents.addAll(it.data.content)

                favoritePostStateLiveData.value = FavoriteRestaurantState.Success(
                    response = contents
                )
                isLast = false
            } else {
                isLast = true
                favoritePostStateLiveData.value = FavoriteRestaurantState.Success(
                    response = contents
                )
            }
        } ?: run {
            favoritePostStateLiveData.value = FavoriteRestaurantState.Error(
                "즐겨찾기 목록을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

}