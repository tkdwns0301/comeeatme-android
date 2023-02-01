package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.bookmark.BookmarkPostResponse
import com.hand.comeeatme.data.response.like.SuccessResponse
import retrofit2.Response
import retrofit2.http.*

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

    @GET("/v1/members/{memberId}/bookmarked")
    suspend fun getAllBookmarked(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
        @Query("page") page: Long,
        @Query("size") size: Long?,
    ): Response<BookmarkPostResponse>
}