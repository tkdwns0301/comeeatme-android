package com.hand.comeeatme.data.repository.member

import com.hand.comeeatme.data.request.member.MemberModifyRequest
import com.hand.comeeatme.data.response.member.MemberDetailResponse
import com.hand.comeeatme.data.response.member.MemberDuplicationResponse
import com.hand.comeeatme.data.response.member.MemberModifyResponse
import com.hand.comeeatme.data.response.member.MemberSearchResponse

interface MemberRepository {
    suspend fun checkNicknameDuplication(accessToken: String, nickname: String) : MemberDuplicationResponse?
    suspend fun modifyMemberInformation(accessToken: String, information: MemberModifyRequest): MemberModifyResponse?
    suspend fun getDetailMember(accessToken: String, memberId: Long) : MemberDetailResponse?
    suspend fun getSearchNicknames(accessToken: String, nickname: String): MemberSearchResponse?
}