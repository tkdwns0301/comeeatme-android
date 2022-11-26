package com.hand.comeeatme.data.repository.comment

import com.hand.comeeatme.data.request.comment.ModifyCommentRequest
import com.hand.comeeatme.data.request.comment.WritingCommentRequest
import com.hand.comeeatme.data.response.comment.CommentListResponse
import com.hand.comeeatme.data.response.comment.CommentResponse

interface CommentRepository {
    suspend fun writingComment(accessToken: String, postId: Long, comment: WritingCommentRequest) : CommentResponse?
    suspend fun modifyComment(accessToken: String, postId: Long, commentId: Long, content: ModifyCommentRequest, ): CommentResponse?
    suspend fun deleteComment(accessToken: String, postId: Long, commentId: Long) : CommentResponse?
    suspend fun getCommentList(accessToken: String, postId: Long, page: Long, size: Long, sort: Boolean) : CommentListResponse?
}