package com.hand.comeeatme.view.main.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class UserViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val bookmarkRepository: BookmarkRepository,
) : BaseViewModel() {
    val userStateLiveData = MutableLiveData<UserState>(UserState.Uninitialized)

    private var profile: String? = null
    private var nickname: String? = null
    private var introduction: String? = null

    private var page: Long = 0
    private var isLast: Boolean = false
    private var contents = arrayListOf<Content>()
    private var sort: String = "id,desc"

    fun setSort(sort: String) {
        this.sort = sort
        getMemberPost(true)
    }

    fun setIsLast(isLast: Boolean) {
        this.isLast = isLast
    }

    fun getIsLast(): Boolean = isLast

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

    fun getMemberPost(isRefresh: Boolean) = viewModelScope.launch {
        if(isRefresh) {
            contents = arrayListOf()
            page = 0
        }

        val response = postRepository.getMemberPost(
            "${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId(),
            page++,
            10,
            sort
        )

        response?.let {
            if(it.data!!.content.isNotEmpty()) {
                contents.addAll(it.data.content)
                userStateLiveData.value = UserState.UserPostSuccess(
                    response = contents,
                )
                isLast = false
            } else {
                isLast = true
                userStateLiveData.value = UserState.UserPostSuccess(
                    response = contents
                )
            }


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


    fun getMemberDetail() = viewModelScope.launch {
        val response = memberRepository.getDetailMember("${appPreferenceManager.getAccessToken()}",
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