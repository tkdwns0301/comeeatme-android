package com.hand.comeeatme.data.repository.member

import com.hand.comeeatme.data.network.MemberService
import com.hand.comeeatme.data.response.home.NicknamesResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMemberRepository(
    private val memberService: MemberService,
    private val ioDispatcher: CoroutineDispatcher,
) : MemberRepository {
    override suspend fun getSearchNicknames(
        accessToken: String,
        nickname: String
    ): NicknamesResponse? = withContext(ioDispatcher) {
        val response = memberService.getSearchNicknames(
            Authorization = "Bearer $accessToken",
            nickname = nickname
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }

}