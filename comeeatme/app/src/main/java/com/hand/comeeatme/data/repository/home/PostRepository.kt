package com.hand.comeeatme.data.repository.home

import com.hand.comeeatme.data.request.Post.NewPostRequest
import com.hand.comeeatme.data.response.post.DetailPostResponse
import com.hand.comeeatme.data.response.post.NewPostResponse
import com.hand.comeeatme.data.response.post.PostResponse

interface PostRepository {
    suspend fun getPosts(accessToken: String, page: Int?, size: Int?, sort: Boolean?, hashTags: List<String>?): PostResponse?
    suspend fun putNewPost(accessToken: String, newPost: NewPostRequest): NewPostResponse?
    suspend fun getDetailPost(accessToken: String, postId: Long) : DetailPostResponse?
    suspend fun getUserPost(accessToken: String, memberId: Long): PostResponse?
}