package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.member.MemberModifyProfileRequest
import com.hand.comeeatme.data.request.member.MemberModifyRequest
import com.hand.comeeatme.data.response.member.*
import retrofit2.Response
import retrofit2.http.*

interface MemberService {
    // 회원 중복 확인
    @GET("/v1/members/duplicate/nickname")
    suspend fun checkNicknameDuplication(
        @Header("Authorization") Authorization: String,
        @Query("nickname") nickname: String,
    ): Response<MemberDuplicationResponse>

    // 회원 수정
    @Headers("content-type: application/json")
    @PATCH("/v1/member")
    suspend fun modifyMemberInformation(
        @Header("Authorization") Authorization: String,
        @Body information: MemberModifyRequest
    ): Response<MemberModifyResponse>

    // 회원 이미지 수정
    @Headers("content-type: application/json")
    @PATCH("/v1/member/image")
    suspend fun modifyMemberProfile(
        @Header("Authorization") Authorization: String,
        @Body modifyProfileRequest: MemberModifyProfileRequest
    ): Response<MemberModifyProfileResponse>

    // 회원 이미지 삭제
    @DELETE("/v1/member/image")
    suspend fun deleteMemberProfile(
        @Header("Authorization") Authorization: String,
    ): Response<MemberModifyProfileResponse>

    // 회원 리스트 조회
    @GET("/v1/members")
    suspend fun getSearchNickname(
        @Header("Authorization") Authorization: String,
        @Query("nickname") nickname: String,
    ): Response<MemberSearchResponse>

    // 회원 상세조회
    @GET("/v1/members/{memberId}")
    suspend fun getDetailUser(
        @Header("Authorization") Authorization: String,
        @Path("memberId") memberId: Long,
    ): Response<MemberDetailResponse>


}