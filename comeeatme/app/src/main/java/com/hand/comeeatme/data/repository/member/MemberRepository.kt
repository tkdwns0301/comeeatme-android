package com.hand.comeeatme.data.repository.member

import com.hand.comeeatme.data.request.member.MemberModifyProfileRequest
import com.hand.comeeatme.data.request.member.MemberModifyRequest
import com.hand.comeeatme.data.request.member.MemberTermRequest
import com.hand.comeeatme.data.request.member.ReasonRequest
import com.hand.comeeatme.data.response.like.SuccessResponse
import com.hand.comeeatme.data.response.member.*

interface MemberRepository {
    suspend fun checkNicknameDuplication(accessToken: String, nickname: String) : MemberDuplicationResponse?
    suspend fun modifyMemberInformation(accessToken: String, information: MemberModifyRequest): MemberModifyResponse?
    suspend fun modifyMemberProfile(accessToken: String, modifyProfile: MemberModifyProfileRequest) : MemberModifyProfileResponse?
    suspend fun getDetailMember(accessToken: String, memberId: Long) : MemberDetailResponse?
    suspend fun getSearchNicknames(accessToken: String, nickname: String): MemberSearchResponse?
    suspend fun setTermsAgree(accessToken: String, agreeOrNot: MemberTermRequest): MemberModifyResponse?
    suspend fun putUnlinkReason(accessToken: String, reason: ReasonRequest) : SuccessResponse?
    suspend fun unLinkService(accessToken: String): MemberModifyResponse?
}