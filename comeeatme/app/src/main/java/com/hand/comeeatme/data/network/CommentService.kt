package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.comment.ModifyCommentRequest
import com.hand.comeeatme.data.request.comment.WritingCommentRequest
import com.hand.comeeatme.data.response.comment.CommentListResponse
import com.hand.comeeatme.data.response.comment.CommentResponse
import retrofit2.Response
import retrofit2.http.*

interface CommentService {
    // 댓글 작성
    @Headers("content-type: application/json")
    @POST("/v1/posts/{postId}/comment")
    suspend fun writingComment(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
        @Body comment: WritingCommentRequest,
    ): Response<CommentResponse>

    // 댓글 수정
    @Headers("content-type: application/json")
    @PATCH("/v1/posts/{postId}/comments/{commentId}")
    suspend fun modifyComment(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
        @Path("commentId") commentId: Long,
        @Body content: ModifyCommentRequest
    ): Response<CommentResponse>

    // 댓글 삭제
    @DELETE("/v1/posts/{postId}/comments/{commentId}")
    suspend fun deleteComment(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
        @Path("commentId") commentId: Long,
    ): Response<CommentResponse>

    @GET("/v1/posts/{postId}/comments")
    suspend fun getCommentList(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
        @Query("page") page: Long,
        @Query("size") size: Long,
        @Query("sort") sort: Boolean,
    ): Response<CommentListResponse>
}