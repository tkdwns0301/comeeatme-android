package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.like.SuccessResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookmarkService {
    @PUT("/v1/member/bookmark/{postId}")
    suspend fun bookmarkPost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
    ): Response<SuccessResponse>

    @DELETE("/v1/member/bookmark/{postId}")
    suspend fun unBookmarkPost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
    ): Response<SuccessResponse>
}