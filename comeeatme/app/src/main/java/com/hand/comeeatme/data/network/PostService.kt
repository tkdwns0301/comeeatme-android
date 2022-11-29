package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.post.ModifyPostRequest
import com.hand.comeeatme.data.request.post.NewPostRequest
import com.hand.comeeatme.data.response.post.DetailPostResponse
import com.hand.comeeatme.data.response.post.NewPostResponse
import com.hand.comeeatme.data.response.post.PostResponse
import retrofit2.Response
import retrofit2.http.*

interface PostService {
    // 게시물 전체 조회
    @GET("/v1/posts")
    suspend fun getAllPost(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int?,
        @Query("sort") sort: Boolean?,
        @Query("hashtags") hashtags: List<String>?,
    ): Response<PostResponse>

    // 회원 게시물 리스트 조회
    @GET("/v1/members/{memberId}/posts")
    suspend fun getUserPost(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
    ) : Response<PostResponse>

    // TODO 음식점 게시물 리스트 조회

    // 게시물 상세조회
    @GET("/v1/posts/{postId}")
    suspend fun getDetailPost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
    ): Response<DetailPostResponse>

    // 게시물 작성
    @Headers("content-type: application/json")
    @POST("/v1/post")
    suspend fun putNewPost(
        @Header("Authorization") Authorization: String,
        @Body newPost: NewPostRequest,
    ): Response<NewPostResponse>

    // 게시물 수정
    @Headers("content-type: application/json")
    @PATCH("/v1/posts/{postId}")
    suspend fun modifyPost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
        @Body modifyPost: ModifyPostRequest
    ): Response<NewPostResponse>

    // 게시물 삭제
    @DELETE("v1/posts/{postId}")
    suspend fun deletePost(
        @Header("Authorization") Authorization: String,
        @Path("postId") postId: Long,
    ): Response<NewPostResponse>
}