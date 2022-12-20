package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.like.SuccessResponse
import com.hand.comeeatme.data.response.post.PostResponse
import retrofit2.Response
import retrofit2.http.*

interface LikeService {
    @PUT("/v1/member/like/{postId}")
    suspend fun likePost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
    ): Response<SuccessResponse>

    @DELETE("/v1/member/like/{postId}")
    suspend fun unLikePost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
    ):Response<SuccessResponse>

    @GET("/v1/members/{memberId}/liked")
    suspend fun getLikedPost(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
    ): Response<PostResponse>
}