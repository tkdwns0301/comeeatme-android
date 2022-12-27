package com.hand.comeeatme.data.repository.post

import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.network.PostService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.request.post.ModifyPostRequest
import com.hand.comeeatme.data.request.post.NewPostRequest
import com.hand.comeeatme.data.response.post.DetailPostResponse
import com.hand.comeeatme.data.response.post.NewPostResponse
import com.hand.comeeatme.data.response.post.PostResponse
import com.hand.comeeatme.data.response.post.RestaurantPostResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultPostRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val postService: PostService,
    private val oAuthService: OAuthService,
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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getPosts(
                    "${appPreferenceManager.getAccessToken()}",
                    page,
                    size,
                    sort,
                    hashTags
                )
            } else {
                null
            }
        } else {
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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getMemberPost(
                    "${appPreferenceManager.getAccessToken()}",
                    memberId,
                    page,
                    size,
                    sort
                )
            } else {
                null
            }

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
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getRestaurantPosts(
                    "${appPreferenceManager.getAccessToken()}",
                    restaurantId
                )
            } else {
                null
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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                putNewPost(
                    "${appPreferenceManager.getAccessToken()}",
                    newPost
                )
            } else {
                null
            }

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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getDetailPost(
                    "${appPreferenceManager.getAccessToken()}",
                    postId
                )
            } else {
                null
            }


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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                modifyPost(
                    "${appPreferenceManager.getAccessToken()}",
                    postId,
                    modifyPost
                )
            } else {
                null
            }


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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                deletePost(
                    "${appPreferenceManager.getAccessToken()}",
                    postId
                )
            } else {
                null
            }


        } else {
            null
        }
    }

}