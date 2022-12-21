package com.hand.comeeatme.view.main.user.other

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class OtherPageViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val bookmarkRepository: BookmarkRepository,
) : BaseViewModel() {

    val otherPageStateLiveData = MutableLiveData<OtherPageState>(OtherPageState.Uninitialized)

    private var profile: String? = null
    private var nickname: String? = null
    private var sort: String = "id,desc"

    fun setSort(sort: String) {
        this.sort = sort
    }

    fun setProfile(profile: String?) {
        this.profile = profile
    }

    fun getProfile(): String? = profile

    fun setNickname(nickname: String?) {
        this.nickname = nickname
    }

    fun getNickname(): String? = nickname

    fun getDetailMember(memberId: Long) = viewModelScope.launch {
        otherPageStateLiveData.value = OtherPageState.Loading

        val response = memberRepository.getDetailMember(
            "${appPreferenceManager.getAccessToken()}",
            memberId
        )

        response?.let {
            otherPageStateLiveData.value = OtherPageState.Success(
                response = it
            )
        } ?: run {
            otherPageStateLiveData.value = OtherPageState.Error(
                "사용자 정보를 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun getMemberPost(memberId: Long) = viewModelScope.launch {
        val response = postRepository.getMemberPost(
            "${appPreferenceManager.getAccessToken()}",
            memberId,
            0,
            10,
            sort
        )

        response?.let {
            otherPageStateLiveData.value = OtherPageState.MemberPostSuccess(
                response = it
            )
        }?:run {
            otherPageStateLiveData.value = OtherPageState.Error(
                "사용자 글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun likePost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = likeRepository.likePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            otherPageStateLiveData.value = OtherPageState.LikePostSuccess
        } ?: run {
            otherPageStateLiveData.value = OtherPageState.Error(
                "좋아요 실패"
            )
        }
    }

    fun unLikePost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = likeRepository.unLikePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            otherPageStateLiveData.value = OtherPageState.UnLikePostSuccess
        } ?: run {
            otherPageStateLiveData.value = OtherPageState.Error(
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
            otherPageStateLiveData.value = OtherPageState.BookmarkPostSuccess
        } ?: run {
            otherPageStateLiveData.value = OtherPageState.Error(
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
            otherPageStateLiveData.value = OtherPageState.UnBookmarkPostSuccess
        } ?: run {
            otherPageStateLiveData.value = OtherPageState.Error(
                "북마크 취소 실패"
            )
        }
    }


}