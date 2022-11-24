package com.hand.comeeatme.view.main.bookmark.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.favorite.FavoriteRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class FavoritePostViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val favoriteRepository: FavoriteRepository,
) : BaseViewModel() {
    val favoritePostStateLiveData =
        MutableLiveData<FavoritePostState>(FavoritePostState.Uninitialized)

    fun favoritePost(
        restaurantId: Long,
    ) = viewModelScope.launch {
        val response = favoriteRepository.favoritePost("${appPreferenceManager.getAccessToken()}",
            restaurantId)

        response?.let {
            favoritePostStateLiveData.value = FavoritePostState.FavoritePostSuccess
        } ?: run {
            favoritePostStateLiveData.value = FavoritePostState.Error(
                "즐겨찾기 실패"
            )
        }
    }

    fun unFavoritePost(
        restaurantId: Long,
    ) = viewModelScope.launch {
        val response = favoriteRepository.unFavoritePost("${appPreferenceManager.getAccessToken()}",
            restaurantId)

        response?.let {
            favoritePostStateLiveData.value = FavoritePostState.FavoritePostSuccess
        } ?: run {
            favoritePostStateLiveData.value = FavoritePostState.Error(
                "즐겨찾기 취소 실패"
            )
        }
    }

    fun getAllFavorite(
        page: Long?,
        size: Long?,
        sort: Boolean?,
    ) = viewModelScope.launch {
        favoritePostStateLiveData.value = FavoritePostState.Loading

        val response = favoriteRepository.getAllFavorite(
            "${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId(),
            page,
            size,
            sort,
        )

        response?.let {
            if (it.data.content.isEmpty()) {
                favoritePostStateLiveData.value = FavoritePostState.Error(
                    "즐겨찾기 목록이 없어요,,,"
                )
            } else {
                favoritePostStateLiveData.value = FavoritePostState.Success(
                    response = it
                )
            }
        } ?: run {
            favoritePostStateLiveData.value = FavoritePostState.Error(
                "즐겨찾기 목록을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

}