package com.hand.comeeatme.view.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.data.repository.logIn.OAuthRepository
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.data.response.logIn.TokenResponse
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val oauthRepository: OAuthRepository,
) : BaseViewModel() {
    companion object {
        const val COMMUNITY_KEY = "Community"
    }

    private val hashTagKorToEng = hashMapOf<String, String>(
        "감성있는" to "MOODY",
        "혼밥" to "EATING_ALON",
        "단체모임" to "GROUP_MEETING",
        "데이트" to "DATE",
        "특별한 날" to "SPECIAL_DAY",
        "신선한 재료" to "FRESH_INGREDIENT",
        "시그니쳐 메뉴" to "SIGNATURE_MENU",
        "가성비" to "COST_EFFECTIVENESS",
        "고급스러운" to "LUXURIOUSNESS",
        "자극적인" to "STRONG_TASTE",
        "친절" to "KINDNESS",
        "청결" to "CLEANLINESS",
        "주차장" to "PARKING",
        "반려동물 동반" to "PET",
        "아이 동반" to "CHILD",
        "24시간" to "AROUND_CLOCK"
    )

    private var page: Int = 0
    private var contents = arrayListOf<Content>()

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        loadPost()
    }

    private var checkedChipList: ArrayList<String> = arrayListOf()

    fun setCheckedChipList(checkedChipList: ArrayList<String>) {
        this.checkedChipList = checkedChipList
    }

    fun getCheckedChipList(): ArrayList<String> = checkedChipList

    fun likePost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = likeRepository.likePost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            homeStateLiveData.value = HomeState.LikePostSuccess
        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "좋아요를 하는 도중 오류가 발생했습니다."
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
                "좋아요를 취소하는 도중 오류가 발생했습니다."
            )
        }
    }

    fun bookmarkPost(
        postId: Long,
    ) = viewModelScope.launch {
        val response =
            bookmarkRepository.bookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            if(it.success) {
                homeStateLiveData.value = HomeState.BookmarkPostSuccess
            } else {
                reissueToken()
            }
        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "북마크를 하는 도중 오류가 발생했습니다."
            )
        }
    }

    fun unBookmarkPost(
        postId: Long,
    ) = viewModelScope.launch {
        val response =
            bookmarkRepository.unBookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            if(it.success) {
                homeStateLiveData.value = HomeState.UnBookmarkPostSuccess
            } else {
                reissueToken()
            }
        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "북마크를 취소하는 도중 오류가 발생했습니다."
            )
        }
    }

    fun loadPost(
    ) = viewModelScope.launch {
        homeStateLiveData.value = HomeState.Loading

        var hashTagsEng: ArrayList<String>? = arrayListOf<String>()

        if (!checkedChipList.isNullOrEmpty()) {
            checkedChipList.forEach { hashTag ->
                hashTagsEng!!.add(hashTagKorToEng[hashTag]!!)
            }
        } else {
            hashTagsEng = null
        }

        val posts = postRepository.getPosts("${appPreferenceManager.getAccessToken()}",
            page++,
            10,
            false,
            hashTagsEng)

        posts?.let {
            if(!it.success) {
                reissueToken()
            } else {
                if(!it.data!!.content.isNullOrEmpty()) {
                    contents.addAll(it.data!!.content)

                    homeStateLiveData.value = HomeState.Success(
                        posts = contents
                    )
                } else {
                    homeStateLiveData.value = HomeState.LikePostSuccess
                }

            }

        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    private fun reissueToken() = viewModelScope.launch {
        val response = oauthRepository.reissueToken(
            "${appPreferenceManager.getRefreshToken()}"
        )

        response?.let {
            saveToken(it)

            homeStateLiveData.value = HomeState.Error(
                "다시 시도해주세요"
            )

        } ?: run {
            homeStateLiveData.value = HomeState.Error(
                "정보를 요청하는 도중 오류가 발생했습니다."
            )
        }
    }

    private fun saveToken(token: TokenResponse) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.putAccessToken(token.accessToken)
            appPreferenceManager.putRefreshToken(token.refreshToken)
            appPreferenceManager.putMemberId(token.memberId!!)
            fetchData()
        }
    }

}