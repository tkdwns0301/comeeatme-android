package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.user.UserModifyRequest
import com.hand.comeeatme.data.response.user.UserDetailResponse
import com.hand.comeeatme.data.response.user.UserDuplicationResponse
import com.hand.comeeatme.data.response.user.UserInformationModifyResponse
import com.hand.comeeatme.data.response.user.UserSearchResultResponse
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("/v1/members/duplicate/nickname")
    suspend fun checkNicknameDuplication(
        @Header("Authorization") Authorization: String,
        @Query("nickname") nickname: String,
    ): Response<UserDuplicationResponse>

    @Headers("content-type: application/json")
    @PATCH("/v1/member")
    suspend fun modifyUserInformation(
        @Header("Authorization") Authorization: String,
        @Body information: UserModifyRequest
    ): Response<UserInformationModifyResponse>

    @GET("/v1/members")
    suspend fun getSearchNickname(
        @Header("Authorization") Authorization: String,
        @Query("nickname") nickname: String,
    ): Response<UserSearchResultResponse>

    @GET("/v1/members/{memberId}")
    suspend fun getDetailUser(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
    ): Response<UserDetailResponse>

}