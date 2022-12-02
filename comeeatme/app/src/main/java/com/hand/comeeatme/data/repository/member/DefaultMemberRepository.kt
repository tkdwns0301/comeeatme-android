package com.hand.comeeatme.data.repository.member

import com.hand.comeeatme.data.network.MemberService
import com.hand.comeeatme.data.request.member.MemberModifyRequest
import com.hand.comeeatme.data.response.member.MemberDetailResponse
import com.hand.comeeatme.data.response.member.MemberDuplicationResponse
import com.hand.comeeatme.data.response.member.MemberModifyResponse
import com.hand.comeeatme.data.response.member.MemberSearchResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMemberRepository(
    private val memberService: MemberService,
    private val ioDispatcher: CoroutineDispatcher,
) : MemberRepository {
    override suspend fun checkNicknameDuplication(
        accessToken: String,
        nickname: String,
    ): MemberDuplicationResponse? = withContext(ioDispatcher) {
        val response = memberService.checkNicknameDuplication(
            Authorization = "Bearer $accessToken",
            nickname = nickname
        )

        if (response.isSuccessful) {
            if (!response.body()!!.success) {
                null
            } else {
                response.body()!!
            }
        } else {
            null
        }
    }

    override suspend fun modifyMemberInformation(
        accessToken: String,
        information: MemberModifyRequest,
    ): MemberModifyResponse? = withContext(ioDispatcher) {
        val response = memberService.modifyMemberInformation(
            Authorization = "Bearer $accessToken",
            information = information
        )

        if (response.isSuccessful) {
            if (!response.body()!!.success) {
                null
            } else {
                response.body()!!
            }
        } else {
            null
        }
    }

    override suspend fun getDetailMember(
        accessToken: String,
        memberId: Long,
    ): MemberDetailResponse? = withContext(ioDispatcher) {
        val response = memberService.getDetailUser(
            Authorization = "Bearer $accessToken",
            memberId = memberId
        )

        if (response.isSuccessful) {
            if (!response.body()!!.success) {
                null
            } else {
                response.body()!!
            }
        } else {
            null
        }

    }


    override suspend fun getSearchNicknames(
        accessToken: String,
        nickname: String,
    ): MemberSearchResponse? = withContext(ioDispatcher) {
        val response = memberService.getSearchNickname(
            Authorization = "Bearer $accessToken",
            nickname = nickname
        )

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

}