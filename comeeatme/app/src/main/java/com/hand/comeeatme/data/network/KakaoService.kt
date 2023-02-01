package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.kakao.KakaoAddressResponse
import com.hand.comeeatme.data.response.kakao.KakaoCoordResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoService {
    @GET("/v2/local/geo/coord2regioncode.json")
    suspend fun getAddress(
        @Header("Authorization") Authorization: String,
        @Query("x") x: String,
        @Query("y") y: String,
    ): Response<KakaoAddressResponse>

    @GET("/v2/local/search/address.json")
    suspend fun getAddressToCoord(
        @Header("Authorization") Authorization: String,
        @Query("query") query: String,
        @Query("analyze_type") analyze_type: String,
    ): Response<KakaoCoordResponse>
}