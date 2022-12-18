package com.hand.comeeatme.data.repository.bookmark

import android.util.Log
import com.hand.comeeatme.data.network.BookmarkService
import com.hand.comeeatme.data.response.bookmark.BookmarkPostResponse
import com.hand.comeeatme.data.response.like.SuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONObject

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
            } else if (response.code() == 401) {
                var jsonObject: JSONObject? = null
                var errorResponse: SuccessResponse? = null

                try {
                    jsonObject = JSONObject(response.errorBody()!!.string())
                    errorResponse =
                        SuccessResponse(success = jsonObject.getBoolean("success"), error = null)
                } catch (e: Exception) {
                    Log.e("error", "${e.printStackTrace()}")
                }
                errorResponse
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
                var jsonObject: JSONObject? = null
                var errorResponse: SuccessResponse? = null

                try {
                    jsonObject = JSONObject(response.errorBody()!!.string())
                    errorResponse = SuccessResponse(success = jsonObject.getBoolean("success"),  error = null)
                } catch (e: Exception) {
                    Log.e("error", "${e.printStackTrace()}")
                }
                errorResponse
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
            var jsonObject: JSONObject? = null
            var errorResponse: BookmarkPostResponse? = null

            try {
                jsonObject = JSONObject(response.errorBody()!!.string())
                errorResponse = BookmarkPostResponse(success = jsonObject.getBoolean("success"), data = null, error = null)
            } catch (e: Exception) {
                Log.e("error", "${e.printStackTrace()}")
            }
            errorResponse
        }
        else {
            null
        }
    }
}