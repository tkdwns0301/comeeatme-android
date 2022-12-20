package com.hand.comeeatme.view.main.user.menu.mycomment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.comment.CommentRepository
import com.hand.comeeatme.data.response.comment.MemberCommentContent
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyCommentViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val commentRepository: CommentRepository,
) : BaseViewModel() {
    val myCommentStateLiveData = MutableLiveData<MyCommentState>(MyCommentState.Uninitialized)

    private var page: Long = 0
    private var contents = arrayListOf<MemberCommentContent>()
    private var isLast: Boolean = false

    override fun fetchData(): Job = viewModelScope.launch {
        getMemberComments(true)
    }

    fun setIsLast(isLast: Boolean) {
        this.isLast = isLast
    }

    fun getIsLast(): Boolean = isLast


    fun getMemberComments(isRefresh: Boolean) = viewModelScope.launch {
        myCommentStateLiveData.value = MyCommentState.Loading

        if (isRefresh) {
            contents = arrayListOf()
            page = 0
        }

        val response = commentRepository.getMemberComments(
            "${appPreferenceManager.getAccessToken()}",
            appPreferenceManager.getMemberId(),
            page++,
            10,
        )

        response?.let {
            if (it.data!!.content.isNotEmpty()) {
                contents.addAll(it.data!!.content)
                myCommentStateLiveData.value = MyCommentState.Success(
                    response = contents
                )
                isLast = false
            } else {
                isLast = true
                myCommentStateLiveData.value = MyCommentState.Success(
                    response = contents
                )
            }
        }?:run {
            myCommentStateLiveData.value = MyCommentState.Error(
                "댓글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }
}