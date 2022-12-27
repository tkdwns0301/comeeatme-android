package com.hand.comeeatme.view.main.user.setting.notice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.notice.NoticeRepository
import com.hand.comeeatme.data.response.notice.NoticesContent
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoticeViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val noticeRepository: NoticeRepository,
): BaseViewModel() {
    val noticeStateLiveData = MutableLiveData<NoticeState>(NoticeState.Loading)

    private var page: Long = 0
    private var contents = arrayListOf<NoticesContent>()
    private var isLast: Boolean = false

    fun getIsLast(): Boolean = isLast
    fun setIsLast(isLast: Boolean) {
        this.isLast = isLast
    }

    override fun fetchData(): Job = viewModelScope.launch {
        getNotices(true)
    }

    fun getNotices(isRefresh: Boolean) = viewModelScope.launch {
        noticeStateLiveData.value = NoticeState.Loading

        val response = noticeRepository.getNotices(
            "${appPreferenceManager.getAccessToken()}",
            page++,
            10,
        )

        response?.let {
            if(it.data.content.isNotEmpty()) {
                contents.addAll(it.data.content)

                noticeStateLiveData.value = NoticeState.Success(
                    response = contents
                )
                isLast = false
            } else {
                isLast = true
                noticeStateLiveData.value = NoticeState.Success(
                    response = contents
                )
            }
        }?: run {
            noticeStateLiveData.value = NoticeState.Error(
                "공지사항을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }


}