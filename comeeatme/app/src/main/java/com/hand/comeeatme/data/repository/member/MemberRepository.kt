package com.hand.comeeatme.data.repository.member

import com.hand.comeeatme.data.request.member.MemberModifyProfileRequest
import com.hand.comeeatme.data.request.member.MemberModifyRequest
import com.hand.comeeatme.data.response.member.*

interface MemberRepository {
    suspend fun checkNicknameDuplication(accessToken: String, nickname: String) : MemberDuplicationResponse?
    suspend fun modifyMemberInformation(accessToken: String, information: MemberModifyRequest): MemberModifyResponse?
    suspend fun modifyMemberProfile(accessToken: String, modifyProfile: MemberModifyProfileRequest) : MemberModifyProfileResponse?
    suspend fun getDetailMember(accessToken: String, memberId: Long) : MemberDetailResponse?
    suspend fun getSearchNicknames(accessToken: String, nickname: String): MemberSearchResponse?
}