package com.hand.comeeatme.view.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.data.repository.post.PostRepository
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

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    private var checkedChipList: ArrayList<String> = arrayListOf()

    fun setCheckedChipList(checkedChipList: ArrayList<String>) {
        this.checkedChipList = checkedChipList
    }

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

        var hashTagsEng : ArrayList<String>? = arrayListOf<String>()

        if(!hashTags.isNullOrEmpty()) {
            hashTags.forEach { hashTag ->
                hashTagsEng!!.add(hashTagKorToEng[hashTag]!!)
            }
        } else {
            hashTagsEng = null
        }

        val posts = postRepository.getPosts("${appPreferenceManager.getAccessToken()}",
            page,
            size,
            sort,
            hashTagsEng)

        posts?.let { post ->
            homeStateLiveData.value = HomeState.Success(
                posts = post
            )
        } ?: run {
            homeStateLiveData.value = HomeState.Error("데이터 없음")
        }
    }

}