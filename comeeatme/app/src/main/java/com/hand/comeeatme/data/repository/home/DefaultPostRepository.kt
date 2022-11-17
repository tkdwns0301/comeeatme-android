package com.hand.comeeatme.data.repository.home

import com.hand.comeeatme.data.network.PostService
import com.hand.comeeatme.data.request.home.NewPostRequest
import com.hand.comeeatme.data.response.home.NewPostResponse
import com.hand.comeeatme.data.response.home.PostResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultPostRepository(
    private val postService: PostService,
    private val ioDispatcher: CoroutineDispatcher
): PostRepository {
    override suspend fun getPosts(
        accessToken: String,
        page: Int?,
        size: Int?,
        sort: Boolean?,
        hashTags: List<String>?
    ): PostResponse? = withContext(ioDispatcher){
        val response = postService.getAllPost(
            Authorization = "Bearer $accessToken",
            page = page!!,
            size = size!!,
            sort = sort!!,
            hashtags = hashTags
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun putNewPost(
        accessToken: String,
        newPost: NewPostRequest,
    ): NewPostResponse? = withContext(ioDispatcher){
        val response = postService.putNewPost(
            Authorization = "Bearer $accessToken",
            newPost = newPost
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

}