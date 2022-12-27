package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.notice.NoticesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NoticeService {
    // 공지사항 리스트 조회
    @GET("/v1/notices")
    suspend fun getNotices(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Long,
        @Query("size") size: Long,
    ): Response<NoticesResponse>
}