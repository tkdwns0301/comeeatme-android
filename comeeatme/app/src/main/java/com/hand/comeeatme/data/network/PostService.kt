package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.Post.NewPostRequest
import com.hand.comeeatme.data.response.post.DetailPostResponse
import com.hand.comeeatme.data.response.post.NewPostResponse
import com.hand.comeeatme.data.response.post.PostResponse
import retrofit2.Response
import retrofit2.http.*

interface PostService {
    @GET("/v1/posts")
    suspend fun getAllPost(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int?,
        @Query("sort") sort: Boolean?,
        @Query("hashtags") hashtags: List<String>?,
    ): Response<PostResponse>

    @Headers("content-type: application/json")
    @POST("/v1/post")
    suspend fun putNewPost(
        @Header("Authorization") Authorization: String,
        @Body newPost: NewPostRequest,
    ): Response<NewPostResponse>

    @GET("/v1/posts/{postId}")
    suspend fun getDetailPost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
    ): Response<DetailPostResponse>

    @GET("/v1/members/{memberId}/posts")
    suspend fun getUserPost(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
    ) : Response<PostResponse>
}