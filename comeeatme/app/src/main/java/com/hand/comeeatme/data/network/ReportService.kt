package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.report.ReportRequest
import com.hand.comeeatme.data.response.member.MemberModifyResponse
import retrofit2.Response
import retrofit2.http.*

interface ReportService {
    // 신고하기
    @Headers("content-type: application/json")
    @POST("/v1/posts/{postId}/report")
    suspend fun reportPost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
        @Body reason: ReportRequest,
    ): Response<MemberModifyResponse>
}