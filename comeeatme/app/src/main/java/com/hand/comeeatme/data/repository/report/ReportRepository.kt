package com.hand.comeeatme.data.repository.report

import com.hand.comeeatme.data.request.report.ReportRequest
import com.hand.comeeatme.data.response.member.MemberModifyResponse

interface ReportRepository {
    suspend fun reportPost(accessToken: String, postId: Long, reason: ReportRequest): MemberModifyResponse?
}