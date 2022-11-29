package com.hand.comeeatme.view.main.user.menu.myreview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class MyReviewViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val postRepository: PostRepository,
) : BaseViewModel() {
    val myReviewStateLiveData = MutableLiveData<MyReviewState>(MyReviewState.Uninitialized)

    fun getUserPost() = viewModelScope.launch {
        myReviewStateLiveData.value = MyReviewState.Loading

        val response = postRepository.getUserPost("${appPreferenceManager.getAccessToken()}", appPreferenceManager.getMemberId())

        response?.let {
            myReviewStateLiveData.value = MyReviewState.Success(
                response = it
            )
        }?: run {
            myReviewStateLiveData.value = MyReviewState.Error(
                "글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

}