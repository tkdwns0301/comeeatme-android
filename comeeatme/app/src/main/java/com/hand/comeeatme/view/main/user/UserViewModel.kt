package com.hand.comeeatme.view.main.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.home.PostRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.data.repository.user.UserRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class UserViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val bookmarkRepository: BookmarkRepository,
) : BaseViewModel() {
    val userStateLiveData = MutableLiveData<UserState>(UserState.Uninitialized)

    private var profile: String? = null
    private var nickname: String? = null
    private var introduction: String? = null

    fun setProfile(profile: String?) {
        this.profile = profile
    }

    fun getProfile() : String? {
        return profile
    }

    fun setNickname(nickname: String) {
        this.nickname = nickname
    }

    fun getNickname() : String {
        return nickname!!
    }

    fun setIntroduction(introduction: String) {
        this.introduction = introduction
    }

    fun getIntroduction() : String? {
        return introduction
    }

    fun getUserPost() = viewModelScope.launch {
        val response = postRepository.getUserPost("${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId())

        response?.let {
            userStateLiveData.value = UserState.UserPostSuccess(
                response = it,
            )
        } ?: run {
            userStateLiveData.value = UserState.Error(
                "사용자 글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun likePost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = likeRepository.likePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            userStateLiveData.value = UserState.LikePostSuccess
        } ?: run {
            userStateLiveData.value = UserState.Error(
                "좋아요 실패"
            )
        }
    }

    fun unLikePost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = likeRepository.unLikePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            userStateLiveData.value = UserState.UnLikePostSuccess
        } ?: run {
            userStateLiveData.value = UserState.Error(
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
            userStateLiveData.value = UserState.BookmarkPostSuccess
        } ?: run {
            userStateLiveData.value = UserState.Error(
                "북마크 실패"
            )
        }
    }

    fun unBookmarkPost(
        postId: Long,
    ) = viewModelScope.launch {
        val response =
            bookmarkRepository.unBookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            userStateLiveData.value = UserState.UnBookmarkPostSuccess
        } ?: run {
            userStateLiveData.value = UserState.Error(
                "북마크 취소 실패"
            )
        }
    }


    fun getUserDetail() = viewModelScope.launch {
        val response = userRepository.getDetailUser("${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId())

        response?.let {
            userStateLiveData.value = UserState.UserDetailSuccess(
                response = it
            )
        } ?: run {
            userStateLiveData.value = UserState.Error(
                "사용자 정보를 불러오는 도중 오류가 발생했습니다."
            )
        }
    }
}