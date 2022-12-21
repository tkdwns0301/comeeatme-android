package com.hand.comeeatme.data.repository.post

import android.util.Log
import com.hand.comeeatme.data.network.PostService
import com.hand.comeeatme.data.request.post.ModifyPostRequest
import com.hand.comeeatme.data.request.post.NewPostRequest
import com.hand.comeeatme.data.response.post.DetailPostResponse
import com.hand.comeeatme.data.response.post.NewPostResponse
import com.hand.comeeatme.data.response.post.PostResponse
import com.hand.comeeatme.data.response.post.RestaurantPostResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONObject

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
        val response = postService.getAllPost(Authorization = "Bearer $accessToken",
            page = page!!,
            size = size!!,
            sort = sort!!,
            hashtags = hashTags)


        if (response.isSuccessful) {
            response.body()!!
        } else if(response.code() == 401) {
            var jsonObject: JSONObject? = null
            var errorResponse: PostResponse? = null

            try {
                jsonObject = JSONObject(response.errorBody()!!.string())
                errorResponse = PostResponse(success = jsonObject.getBoolean("success"), data = null, error = null)
            } catch (e: Exception) {
                Log.e("error", "${e.printStackTrace()}")
            }
            errorResponse
        }

        else {
            null
        }
    }

    override suspend fun getMemberPost(
        accessToken: String,
        memberId: Long,
        page: Long,
        size: Long,
        sort: String,
    ): PostResponse? = withContext(ioDispatcher) {
        val response =
            postService.getUserPost(
                Authorization = "Bearer $accessToken",
                memberId = memberId,
                page = page,
                size = size,
                sort = sort
            )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun getRestaurantPosts(
        accessToken: String,
        restaurantId: Long,
    ): RestaurantPostResponse? = withContext(ioDispatcher) {
        val response = postService.getRestaurantPosts(
            Authorization = "Bearer $accessToken",
            restaurantId = restaurantId
        )

        if (response.isSuccessful) {
            if (!response.body()!!.success) {
                null
            } else {
                response.body()!!
            }
        } else {
            null
        }
    }

    override suspend fun putNewPost(
        accessToken: String,
        newPost: NewPostRequest,
    ): NewPostResponse? = withContext(ioDispatcher) {
        val response =
            postService.putNewPost(Authorization = "Bearer $accessToken", newPost = newPost)

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun getDetailPost(
        accessToken: String,
        postId: Long,
    ): DetailPostResponse? = withContext(ioDispatcher) {
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


    override suspend fun modifyPost(
        accessToken: String,
        postId: Long,
        modifyPost: ModifyPostRequest,
    ): NewPostResponse? = withContext(ioDispatcher) {
        val response = postService.modifyPost(Authorization = "Bearer $accessToken",
            postId = postId,
            modifyPost = modifyPost)

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun deletePost(
        accessToken: String,
        postId: Long,
    ): NewPostResponse? = withContext(ioDispatcher) {
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