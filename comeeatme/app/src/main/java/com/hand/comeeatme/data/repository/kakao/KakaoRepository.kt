package com.hand.comeeatme.data.repository.kakao

import com.hand.comeeatme.data.response.kakao.KakaoAddressResponse

interface KakaoRepository {
    suspend fun getAddress(restApiKey: String, x: String, y: String ): KakaoAddressResponse?

}