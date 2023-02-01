package com.hand.comeeatme.view.main.user.menu.myreview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class MyReviewViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val postRepository: PostRepository,
) : BaseViewModel() {
    val myReviewStateLiveData = MutableLiveData<MyReviewState>(MyReviewState.Uninitialized)

    private var page: Long = 0
    private var contents = arrayListOf<Content>()
    private var isLast: Boolean = false

    fun getIsLast(): Boolean = isLast
    fun setIsLast(isLast: Boolean) {
        this.isLast = isLast
    }

    fun getUserPost(isRefresh: Boolean) = viewModelScope.launch {
        myReviewStateLiveData.value = MyReviewState.Loading

        if(isRefresh) {
            page = 0
            contents = arrayListOf()
        }

        val response = postRepository.getMemberPost(
            "${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId(),
            page++,
            10,
            "id,desc"
        )

        response?.let {
            if(it.data!!.content.isNotEmpty()) {
                contents.addAll(it.data.content)

                myReviewStateLiveData.value = MyReviewState.Success(
                    response = contents
                )
                isLast = false
            } else {
                isLast = true
                myReviewStateLiveData.value = MyReviewState.Success(
                    response = contents
                )
            }

        }?: run {
            myReviewStateLiveData.value = MyReviewState.Error(
                "글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

}