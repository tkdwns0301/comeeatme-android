package com.hand.comeeatme.data.repository.user

import com.hand.comeeatme.data.network.UserService
import com.hand.comeeatme.data.request.user.UserModifyRequest
import com.hand.comeeatme.data.response.user.UserDetailResponse
import com.hand.comeeatme.data.response.user.UserDuplicationResponse
import com.hand.comeeatme.data.response.user.UserInformationModifyResponse
import com.hand.comeeatme.data.response.user.UserSearchResultResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val userService: UserService,
    private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {
    override suspend fun checkNicknameDuplication(
        accessToken: String,
        nickname: String,
    ): UserDuplicationResponse? = withContext(ioDispatcher) {
        val response = userService.checkNicknameDuplication(
            Authorization = "Bearer $accessToken",
            nickname = nickname
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun modifyUserInformation(
        accessToken: String,
        information: UserModifyRequest,
    ): UserInformationModifyResponse? = withContext(ioDispatcher) {
        val response = userService.modifyUserInformation(
            Authorization = "Bearer $accessToken",
            information = information
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun getSearchNickname(
        accessToken: String,
        nickname: String,
    ): UserSearchResultResponse? = withContext(ioDispatcher) {
        val response = userService.getSearchNickname(
            Authorization = "Bearer $accessToken",
            nickname = nickname,
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun getDetailUser(accessToken: String, memberId: Long): UserDetailResponse? =
        withContext(ioDispatcher) {
            val response = userService.getDetailUser(
                Authorization = "Bearer $accessToken",
                memberId = memberId,
            )

            if (response.isSuccessful) {
                response.body()!!
            } else {
                null
            }
        }
}