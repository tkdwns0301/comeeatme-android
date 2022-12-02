package com.hand.comeeatme.data.repository.comment

import com.hand.comeeatme.data.network.CommentService
import com.hand.comeeatme.data.request.comment.ModifyCommentRequest
import com.hand.comeeatme.data.request.comment.WritingCommentRequest
import com.hand.comeeatme.data.response.comment.CommentListResponse
import com.hand.comeeatme.data.response.comment.CommentResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultCommentRepository(
    private val commentService: CommentService,
    private val ioDispatcher: CoroutineDispatcher,
) : CommentRepository {
    override suspend fun writingComment(
        accessToken: String,
        postId: Long,
        comment: WritingCommentRequest,
    ): CommentResponse? = withContext(ioDispatcher) {
        val response = commentService.writingComment(
            Authorization = "Bearer $accessToken",
            postId = postId,
            comment = comment)

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun modifyComment(
        accessToken: String,
        postId: Long,
        commentId: Long,
        content: ModifyCommentRequest,
    ): CommentResponse? = withContext(ioDispatcher) {
        val response = commentService.modifyComment(
            Authorization = "Bearer $accessToken",
            postId = postId,
            commentId = commentId,
            content = content
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun deleteComment(
        accessToken: String,
        postId: Long,
        commentId: Long,
    ): CommentResponse? = withContext(ioDispatcher) {
        val response = commentService.deleteComment(
            Authorization = "Bearer $accessToken",
            postId = postId,
            commentId = commentId
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }

    }

    override suspend fun getCommentList(
        accessToken: String,
        postId: Long,
        page: Long,
        size: Long,
        sort: Boolean,
    ): CommentListResponse? = withContext(ioDispatcher) {
        val response = commentService.getCommentList(
            Authorization = "Bearer $accessToken",
            postId = postId,
            page = page,
            size = size,
            sort = sort,
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

}