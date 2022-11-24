package com.hand.comeeatme.data.repository.bookmark

import com.hand.comeeatme.data.response.bookmark.BookmarkPostResponse
import com.hand.comeeatme.data.response.like.SuccessResponse

interface BookmarkRepository {
    suspend fun bookmarkPost(accessToken: String, postId: Long): SuccessResponse?
    suspend fun unBookmarkPost(accessToken: String, postId: Long): SuccessResponse?
    suspend fun getAllBookmarked(
        accessToken: String,
        memberId: Long,
        page: Long?,
        size: Long?,
        sort: Boolean?,
    ): BookmarkPostResponse?
}