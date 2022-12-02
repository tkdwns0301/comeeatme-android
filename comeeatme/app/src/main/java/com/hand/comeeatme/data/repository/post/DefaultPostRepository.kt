package com.hand.comeeatme.data.repository.post

import com.hand.comeeatme.data.network.PostService
import com.hand.comeeatme.data.request.post.ModifyPostRequest
import com.hand.comeeatme.data.request.post.NewPostRequest
import com.hand.comeeatme.data.response.post.DetailPostResponse
import com.hand.comeeatme.data.response.post.NewPostResponse
import com.hand.comeeatme.data.response.post.PostResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultPostRepository(
    private val postService: PostService,
    private val ioDispatcher: CoroutineDispatcher,
) : PostRepository {
    override suspend fun getPosts(
        accessToken: String,
        page: Int?,
        size: Int?,
        sort: Boolean?,
        hashTags: List<String>?,
    ): PostResponse? = withContext(ioDispatcher) {
        val response = postService.getAllPost(
            Authorization = "Bearer $accessToken",
            page = page!!,
            size = size!!,
            sort = sort!!,
            hashtags = hashTags
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun putNewPost(
        accessToken: String,
        newPost: NewPostRequest,
    ): NewPostResponse? = withContext(ioDispatcher) {
        val response = postService.putNewPost(
            Authorization = "Bearer $accessToken",
            newPost = newPost
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun getDetailPost(accessToken: String, postId: Long): DetailPostResponse? =
        withContext(ioDispatcher) {
            val response = postService.getDetailPost(
                Authorization = "Bearer $accessToken",
                postId = postId,
            )

            if (response.isSuccessful) {
                response.body()!!
            } else {
                null
            }
        }

    override suspend fun getUserPost(accessToken: String, memberId: Long): PostResponse? =
        withContext(ioDispatcher) {
            val response = postService.getUserPost(
                Authorization = "Bearer $accessToken",
                memberId = memberId
            )

            if (response.isSuccessful) {
                response.body()!!
            } else {
                null
            }
        }

    override suspend fun modifyPost(
        accessToken: String,
        postId: Long,
        modifyPost: ModifyPostRequest,
    ): NewPostResponse? = withContext(ioDispatcher) {
        val response = postService.modifyPost(
            Authorization = "Bearer $accessToken",
            postId = postId,
            modifyPost = modifyPost
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun deletePost(accessToken: String, postId: Long): NewPostResponse? =
        withContext(ioDispatcher) {
            val response = postService.deletePost(
                Authorization = "Bearer $accessToken",
                postId = postId,
            )

            if (response.isSuccessful) {
                response.body()!!
            } else {
                null
            }
        }

}