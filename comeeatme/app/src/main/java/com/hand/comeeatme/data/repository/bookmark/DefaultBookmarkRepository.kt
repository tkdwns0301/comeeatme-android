package com.hand.comeeatme.data.repository.bookmark

import com.hand.comeeatme.data.network.BookmarkService
import com.hand.comeeatme.data.response.bookmark.BookmarkPostResponse
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

    override suspend fun getAllBookmarked(
        accessToken: String,
        memberId: Long,
        page: Long?,
        size: Long?,
        sort: Boolean?,
    ): BookmarkPostResponse?  = withContext(ioDispatcher) {
        val response = bookmarkService.getAllBookmarked(
            Authorization = "Bearer $accessToken",
            memberId = memberId,
            page = page!!,
            size = size!!,
            sort = sort!!,
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }
}