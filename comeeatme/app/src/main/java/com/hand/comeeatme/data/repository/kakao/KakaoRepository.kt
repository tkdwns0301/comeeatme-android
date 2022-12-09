package com.hand.comeeatme.data.repository.kakao

import com.hand.comeeatme.data.response.kakao.KakaoAddressResponse
import com.hand.comeeatme.data.response.kakao.KakaoCoordResponse

interface KakaoRepository {
    suspend fun getAddress(restApiKey: String, x: String, y: String ): KakaoAddressResponse?
    suspend fun getAddressToCoord(restApiKey: String, query: String) : KakaoCoordResponse?

}