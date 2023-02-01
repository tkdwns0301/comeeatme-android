package com.hand.comeeatme.data.repository.like

import com.hand.comeeatme.data.response.like.SuccessResponse
import com.hand.comeeatme.data.response.post.PostResponse

interface LikeRepository {
    suspend fun likePost(accessToken: String, postId: Long) : SuccessResponse?
    suspend fun unLikePost(accessToken: String, postId: Long) : SuccessResponse?
    suspend fun getLikedPosts(accessToken: String, memberId:Long) : PostResponse?
}