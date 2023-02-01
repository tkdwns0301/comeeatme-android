package com.hand.comeeatme.data.repository.bookmark

import com.hand.comeeatme.data.network.BookmarkService
import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.response.bookmark.BookmarkPostResponse
import com.hand.comeeatme.data.response.like.SuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultBookmarkRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val bookmarkService: BookmarkService,
    private val ioDispatcher: CoroutineDispatcher,
    private val oAuthService: OAuthService,

) : BookmarkRepository {
    override suspend fun bookmarkPost(accessToken: String, postId: Long): SuccessResponse? =
        withContext(ioDispatcher) {
            val response = bookmarkService.bookmarkPost(
                Authorization = "Bearer $accessToken",
                postId = postId
            )

            if (response.isSuccessful) {
                response.body()!!
            } else if (response.code() == 401) {
                val response2 = oAuthService.reissueToken(
                    "Bearer ${appPreferenceManager.getRefreshToken()}"
                )

                if(response2.isSuccessful) {
                    appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                    appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                    bookmarkPost(
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

    override suspend fun unBookmarkPost(accessToken: String, postId: Long): SuccessResponse? =
        withContext(ioDispatcher) {
            val response = bookmarkService.unBookmarkPost(
                Authorization = "Bearer $accessToken",
                postId = postId
            )

            if (response.isSuccessful) {
                response.body()!!
            } else if(response.code() == 401) {
                val response2 = oAuthService.reissueToken(
                    "Bearer ${appPreferenceManager.getRefreshToken()}"
                )

                if(response2.isSuccessful) {
                    appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                    appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                    unBookmarkPost(
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

    override suspend fun getAllBookmarked(
        accessToken: String,
        memberId: Long,
        page: Long?,
        size: Long?,
    ): BookmarkPostResponse? = withContext(ioDispatcher) {
        val response = bookmarkService.getAllBookmarked(
            Authorization = "Bearer $accessToken",
            memberId = memberId,
            page = page!!,
            size = size!!,
        )

        if (response.isSuccessful) {
            response.body()!!
        } else if(response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if(response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getAllBookmarked(
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