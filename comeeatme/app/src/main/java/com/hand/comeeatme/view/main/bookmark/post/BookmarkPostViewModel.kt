package com.hand.comeeatme.view.main.bookmark.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class BookmarkPostViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val bookmarkRepository: BookmarkRepository,
): BaseViewModel() {
    val bookmarkPostStateLiveData = MutableLiveData<BookmarkPostState>(BookmarkPostState.Uninitialized)

    fun bookmarkPost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = bookmarkRepository.bookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            bookmarkPostStateLiveData.value = BookmarkPostState.BookmarkPostSuccess
        }?:run {
            bookmarkPostStateLiveData.value = BookmarkPostState.Error(
                "북마크 실패"
            )
        }
    }

    fun unBookmarkPost(
        postId: Long,
    ) = viewModelScope.launch {
        val response = bookmarkRepository.unBookmarkPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            bookmarkPostStateLiveData.value = BookmarkPostState.BookmarkPostSuccess
        }?:run {
            bookmarkPostStateLiveData.value = BookmarkPostState.Error(
                "북마크 취소 실패"
            )
        }
    }

    fun getAllBookmarked(
        page: Long?,
        size: Long?,
    ) = viewModelScope.launch {
        bookmarkPostStateLiveData.value = BookmarkPostState.Loading

        val response = bookmarkRepository.getAllBookmarked(
            "${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId(),
            page,
            size,
        )

        response?.let {
            bookmarkPostStateLiveData.value = BookmarkPostState.Success(
                response = it
            )
        }?:run {
            bookmarkPostStateLiveData.value = BookmarkPostState.Error(
                "북마크한 목록을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }


}