package com.hand.comeeatme.data.repository.member

import com.hand.comeeatme.data.network.MemberService
import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.request.member.MemberModifyProfileRequest
import com.hand.comeeatme.data.request.member.MemberModifyRequest
import com.hand.comeeatme.data.request.member.MemberTermRequest
import com.hand.comeeatme.data.request.member.ReasonRequest
import com.hand.comeeatme.data.response.like.SuccessResponse
import com.hand.comeeatme.data.response.member.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMemberRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val oAuthService: OAuthService,
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
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                checkNicknameDuplication(
                    "${appPreferenceManager.getAccessToken()}",
                    nickname
                )
            } else {
                null
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
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                modifyMemberInformation(
                    "${appPreferenceManager.getAccessToken()}",
                    information
                )
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun modifyMemberProfile(
        accessToken: String,
        modifyProfile: MemberModifyProfileRequest,
    ): MemberModifyProfileResponse? = withContext(ioDispatcher) {
        val response = memberService.modifyMemberProfile(
            Authorization = "Bearer $accessToken",
            modifyProfileRequest = modifyProfile
        )

        if (response.isSuccessful) {
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                modifyMemberProfile(
                    "${appPreferenceManager.getAccessToken()}",
                    modifyProfile
                )
            } else {
                null
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
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getDetailMember(
                    "${appPreferenceManager.getAccessToken()}",
                    memberId,
                )
            } else {
                null
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

        if (response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

    override suspend fun putUnlinkReason(
        accessToken: String,
        reason: ReasonRequest
    ): SuccessResponse? = withContext(ioDispatcher) {
        val response = memberService.putUnLinkReason(
            Authorization = "Bearer $accessToken",
            reason = reason
        )

        if(response.isSuccessful) {
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                putUnlinkReason(
                    "${appPreferenceManager.getAccessToken()}",
                    reason,
                )
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun unLinkService(
        accessToken: String,
    ): MemberModifyResponse? = withContext(ioDispatcher) {
        val response = memberService.unLinkService(
            "Bearer $accessToken"
        )

        if (response.isSuccessful) {
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                unLinkService(
                    "${appPreferenceManager.getAccessToken()}",
                )
            } else {
                null
            }
        }else {
            null
        }
    }

}