package com.hand.comeeatme.data.repository.report

import com.hand.comeeatme.data.network.ReportService
import com.hand.comeeatme.data.request.report.ReportRequest
import com.hand.comeeatme.data.response.member.MemberModifyResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultReportRepository(
    private val reportService: ReportService,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportRepository {
    override suspend fun reportPost(
        accessToken: String,
        postId: Long,
        reason: ReportRequest,
    ): MemberModifyResponse? = withContext(ioDispatcher) {
        val response = reportService.reportPost(
            Authorization = "Bearer $accessToken",
            postId = postId,
            reason = reason
        )

        if (response.isSuccessful) {
            if (!response.body()!!.success) {
                null
            } else {
                response.body()!!
            }
        } else {
            null
        }

    }
}