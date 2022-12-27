package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.comment.ModifyCommentRequest
import com.hand.comeeatme.data.request.comment.WritingCommentRequest
import com.hand.comeeatme.data.response.comment.CommentListResponse
import com.hand.comeeatme.data.response.comment.CommentResponse
import com.hand.comeeatme.data.response.comment.MemberCommentsResponse
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
        @Body content: ModifyCommentRequest,
    ): Response<CommentResponse>

    // 댓글 삭제
    @DELETE("/v1/posts/{postId}/comments/{commentId}")
    suspend fun deleteComment(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
        @Path("commentId") commentId: Long,
    ): Response<CommentResponse>

    // 포스트 댓글 리스트 조회
    @GET("/v1/posts/{postId}/comments")
    suspend fun getCommentList(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
        @Query("page") page: Long,
        @Query("size") size: Long,
    ): Response<CommentListResponse>

    // 회원 댓글 리스트 조회
    @GET("/v1/members/{memberId}/comments")
    suspend fun getMemberComments(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
        @Query("page") page: Long,
        @Query("size") size: Long,
    ): Response<MemberCommentsResponse>
}