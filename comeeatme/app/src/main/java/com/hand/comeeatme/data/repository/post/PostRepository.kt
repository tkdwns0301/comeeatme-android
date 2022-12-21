package com.hand.comeeatme.data.repository.post

import com.hand.comeeatme.data.request.post.ModifyPostRequest
import com.hand.comeeatme.data.request.post.NewPostRequest
import com.hand.comeeatme.data.response.post.DetailPostResponse
import com.hand.comeeatme.data.response.post.NewPostResponse
import com.hand.comeeatme.data.response.post.PostResponse
import com.hand.comeeatme.data.response.post.RestaurantPostResponse

interface PostRepository {
    suspend fun getPosts(
        accessToken: String,
        page: Int?,
        size: Int?,
        sort: Boolean?,
        hashTags: List<String>?,
    ): PostResponse?
    suspend fun getMemberPost(accessToken: String, memberId: Long, page: Long, size: Long, sort: String): PostResponse?
    suspend fun getRestaurantPosts(accessToken: String, restaurantId: Long): RestaurantPostResponse?
    suspend fun getDetailPost(accessToken: String, postId: Long): DetailPostResponse?
    suspend fun putNewPost(accessToken: String, newPost: NewPostRequest): NewPostResponse?
    suspend fun modifyPost(
        accessToken: String,
        postId: Long,
        modifyPost: ModifyPostRequest,
    ): NewPostResponse?
    suspend fun deletePost(accessToken: String, postId: Long): NewPostResponse?
}