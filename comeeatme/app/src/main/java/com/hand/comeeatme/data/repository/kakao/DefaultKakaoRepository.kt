package com.hand.comeeatme.data.repository.kakao

import com.hand.comeeatme.data.network.KakaoService
import com.hand.comeeatme.data.response.kakao.KakaoAddressResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultKakaoRepository(
    private val kakaoService: KakaoService,
    private val ioDispatcher: CoroutineDispatcher,
) : KakaoRepository {
    override suspend fun getAddress(
        restApiKey: String,
        x: String,
        y: String
    ): KakaoAddressResponse? = withContext(ioDispatcher){
        val response = kakaoService.getAddress(
            Authorization = "KakaoAK $restApiKey",
            x = x,
            y = y,
        )

        if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }
}