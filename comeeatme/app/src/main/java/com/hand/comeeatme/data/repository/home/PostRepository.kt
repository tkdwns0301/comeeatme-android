package com.hand.comeeatme.data.repository.home

import com.hand.comeeatme.data.request.home.NewPostRequest
import com.hand.comeeatme.data.response.home.NewPostResponse
import com.hand.comeeatme.data.response.home.PostResponse

interface PostRepository {
    suspend fun getPosts(accessToken: String, page: Int?, size: Int?, sort: Boolean?, hashTags: List<String>?): PostResponse?
    suspend fun putNewPost(accessToken: String, newPost: NewPostRequest): NewPostResponse?
}