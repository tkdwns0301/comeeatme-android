package com.hand.comeeatme.view.main.user.menu.likepost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LikedPostViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val likeRepository: LikeRepository,
) : BaseViewModel() {
    val likedPostStateLiveData = MutableLiveData<LikedPostState>(LikedPostState.Uninitialized)


    override fun fetchData(): Job = viewModelScope.launch {
        getLikedPosts()
    }

    fun getLikedPosts() = viewModelScope.launch {
        likedPostStateLiveData.value = LikedPostState.Loading

        val response = likeRepository.getLikedPosts(
            "${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId()
        )

        response?.let {
            likedPostStateLiveData.value = LikedPostState.Success(
                response = it
            )
        } ?: run {
            likedPostStateLiveData.value = LikedPostState.Error(
                "좋아요한 게시글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

}