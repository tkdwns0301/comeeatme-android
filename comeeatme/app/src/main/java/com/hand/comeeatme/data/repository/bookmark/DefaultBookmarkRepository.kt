package com.hand.comeeatme.data.repository.bookmark

import com.hand.comeeatme.data.network.BookmarkService
import com.hand.comeeatme.data.response.like.SuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultBookmarkRepository(
    private val bookmarkService: BookmarkService,
    private val ioDispatcher: CoroutineDispatcher,
) : BookmarkRepository {
    override suspend fun bookmarkPost(accessToken: String, postId: Long): SuccessResponse? =
        withContext(ioDispatcher) {
            val response = bookmarkService.bookmarkPost(
                Authorization = "Bearer $accessToken",
                postId = postId
            )

            if (response.isSuccessful) {
                response.body()!!
            } else {
                null
            }

        }

    override suspend fun unBookmarkPost(accessToken: String, postId: Long): SuccessResponse? = withContext(ioDispatcher){
        val response = bookmarkService.unBookmarkPost(
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