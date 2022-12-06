package com.hand.comeeatme.view.main.home.post.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.report.ReportRepository
import com.hand.comeeatme.data.request.report.ReportRequest
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class ReportViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val reportRepository: ReportRepository,
) : BaseViewModel() {
    val reportStateLiveData = MutableLiveData<ReportState>(ReportState.Uninitialized)

    fun sendReport(reason: String, postId: Long) = viewModelScope.launch {
        reportStateLiveData.value = ReportState.Loading

        val report = ReportRequest(reason)
        val response = reportRepository.reportPost(
            "${appPreferenceManager.getAccessToken()}",
            postId,
            report
        )

        response?.let {
            reportStateLiveData.value = ReportState.Success
        } ?:run {
            reportStateLiveData.value = ReportState.Error(
                "신고를 하는 도중 오류가 발생했습니다."
            )
        }
    }

}