package com.hand.comeeatme.data.repository.like

import com.hand.comeeatme.data.network.LikeService
import com.hand.comeeatme.data.response.like.SuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultLikeRepository(
    private val likeService: LikeService,
    private val ioDispatcher: CoroutineDispatcher,
) : LikeRepository {
    override suspend fun likePost(accessToken: String, postId: Long): SuccessResponse? =
        withContext(ioDispatcher) {
            val response = likeService.likePost(
                Authorization = "Bearer $accessToken",
                postId = postId
            )

            if (response.isSuccessful) {
                response.body()!!
            } else {
                null
            }

        }

    override suspend fun unLikePost(accessToken: String, postId: Long): SuccessResponse? = withContext(ioDispatcher){
        val response = likeService.unLikePost(
            Authorization = "Bearer $accessToken",
            postId = postId
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }
}