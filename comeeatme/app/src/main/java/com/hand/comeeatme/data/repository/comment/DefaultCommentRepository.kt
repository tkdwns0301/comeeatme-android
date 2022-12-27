package com.hand.comeeatme.data.repository.comment

import com.hand.comeeatme.data.network.CommentService
import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.request.comment.ModifyCommentRequest
import com.hand.comeeatme.data.request.comment.WritingCommentRequest
import com.hand.comeeatme.data.response.comment.CommentListResponse
import com.hand.comeeatme.data.response.comment.CommentResponse
import com.hand.comeeatme.data.response.comment.MemberCommentsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultCommentRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val oAuthService: OAuthService,
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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                writingComment(
                    "${appPreferenceManager.getAccessToken()}",
                    postId,
                    comment
                )
            } else {
                null
            }
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
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                modifyComment(
                    "${appPreferenceManager.getAccessToken()}",
                    postId,
                    commentId,
                    content
                )
            } else {
                null
            }
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
        }else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                deleteComment(
                    "${appPreferenceManager.getAccessToken()}",
                    postId,
                    commentId,
                )
            } else {
                null
            }
        }  else {
            null
        }

    }

    override suspend fun getCommentList(
        accessToken: String,
        postId: Long,
        page: Long,
        size: Long,
    ): CommentListResponse? = withContext(ioDispatcher) {
        val response = commentService.getCommentList(
            Authorization = "Bearer $accessToken",
            postId = postId,
            page = page,
            size = size,
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

                getCommentList(
                    "${appPreferenceManager.getAccessToken()}",
                    postId,
                    page,
                    size,
                )
            } else {
                null
            }
        }  else {
            null
        }
    }

    override suspend fun getMemberComments(
        accessToken: String,
        memberId: Long,
        page: Long,
        size: Long,
    ): MemberCommentsResponse? = withContext(ioDispatcher) {
        val response = commentService.getMemberComments(
            Authorization = "Bearer $accessToken",
            memberId = memberId,
            page = page,
            size = size,
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

                getMemberComments(
                    "${appPreferenceManager.getAccessToken()}",
                    memberId,
                    page,
                    size
                )
            } else {
                null
            }
        }

        else {
            null
        }
    }

}