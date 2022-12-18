package com.hand.comeeatme.data.repository.member

import com.hand.comeeatme.data.network.MemberService
import com.hand.comeeatme.data.request.member.MemberModifyProfileRequest
import com.hand.comeeatme.data.request.member.MemberModifyRequest
import com.hand.comeeatme.data.request.member.MemberTermRequest
import com.hand.comeeatme.data.response.member.*
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

    override suspend fun modifyMemberProfile(
        accessToken: String,
        modifyProfile: MemberModifyProfileRequest
    ): MemberModifyProfileResponse? = withContext(ioDispatcher){
        val response = memberService.modifyMemberProfile(
            Authorization = "Bearer $accessToken",
            modifyProfileRequest =  modifyProfile
        )

        if(response.isSuccessful) {
            if(!response.body()!!.success) {
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

    override suspend fun setTermsAgree(
        accessToken: String,
        agreeOrNot: MemberTermRequest,
    ): MemberModifyResponse? = withContext(ioDispatcher) {
        val response = memberService.setTermsAgree(
            Authorization = "Bearer $accessToken",
            agreeOrNot = agreeOrNot,
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun withdrawalService(
        accessToken: String
    ): MemberModifyResponse? = withContext(ioDispatcher) {
        val response = memberService.withdrawalService(
            "Bearer $accessToken"
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

}