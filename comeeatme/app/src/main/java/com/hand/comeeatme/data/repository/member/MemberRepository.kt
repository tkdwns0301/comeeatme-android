package com.hand.comeeatme.data.repository.member

import com.hand.comeeatme.data.response.home.NicknamesResponse

interface MemberRepository {
    suspend fun getSearchNicknames(accessToken: String, nickname: String): NicknamesResponse?
}