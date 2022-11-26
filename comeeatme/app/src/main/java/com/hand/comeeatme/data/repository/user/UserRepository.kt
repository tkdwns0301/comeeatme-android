package com.hand.comeeatme.data.repository.user

import com.hand.comeeatme.data.request.user.UserModifyRequest
import com.hand.comeeatme.data.response.user.UserDetailResponse
import com.hand.comeeatme.data.response.user.UserDuplicationResponse
import com.hand.comeeatme.data.response.user.UserInformationModifyResponse
import com.hand.comeeatme.data.response.user.UserSearchResultResponse

interface UserRepository {
    suspend fun checkNicknameDuplication(accessToken: String, nickname: String): UserDuplicationResponse?
    suspend fun modifyUserInformation(accessToken: String, information: UserModifyRequest): UserInformationModifyResponse?
    suspend fun getSearchNickname(accessToken: String, nickname: String): UserSearchResultResponse?
    suspend fun getDetailUser(accessToken: String, memberId: Long): UserDetailResponse?
}