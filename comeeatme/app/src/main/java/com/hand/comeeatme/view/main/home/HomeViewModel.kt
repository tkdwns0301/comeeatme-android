package com.hand.comeeatme.view.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.home.PostRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val bookmarkRepository: BookmarkRepository,
) : BaseViewModel() {
    companion object {
        const val COMMUNITY_KEY = "Community"
    }

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun likePost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = likeRepository.likePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            homeStateLiveData.value = HomeState.LikePostSuccess
        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "좋아요 실패"
            )
        }
    }

    fun unLikePost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = likeRepository.unLikePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            homeStateLiveData.value = HomeState.UnLikePostSuccess
        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "좋아요 취소 실패"
            )
        }
    }

    fun bookmarkPost(
        postId: Long,
    ) = viewModelScope.launch {
        val response =
            bookmarkRepository.bookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            homeStateLiveData.value = HomeState.BookmarkPostSuccess
        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "북마크 취소 실패"
            )
        }
    }

    fun unBookmarkPost(
        postId: Long,
    ) = viewModelScope.launch {
        val response =
            bookmarkRepository.unBookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            homeStateLiveData.value = HomeState.BookmarkPostSuccess
        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "북마크 취소 실패"
            )
        }
    }

    fun loadPost(
        page: Int?,
        size: Int?,
        sort: Boolean?,
        hashTags: List<String>?,
    ) = viewModelScope.launch {
        homeStateLiveData.value = HomeState.Loading

        val posts = postRepository.getPosts("${appPreferenceManager.getAccessToken()}",
            page,
            size,
            sort,
            hashTags)

        posts?.let { post ->
            homeStateLiveData.value = HomeState.Success(
                posts = post
            )
        } ?: run {
            homeStateLiveData.value = HomeState.Error("데이터 없음")
        }
    }

}