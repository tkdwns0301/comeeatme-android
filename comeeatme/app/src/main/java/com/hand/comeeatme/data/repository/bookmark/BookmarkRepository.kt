package com.hand.comeeatme.data.repository.bookmark

import com.hand.comeeatme.data.response.like.SuccessResponse

interface BookmarkRepository {
    suspend fun bookmarkPost(accessToken: String, postId: Long) : SuccessResponse?
    suspend fun unBookmarkPost(accessToken: String, postId: Long) : SuccessResponse?
}