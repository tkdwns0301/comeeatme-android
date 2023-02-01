package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.request.code.AddressCodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CodeService {
    // 법정동 코드 조회
    @GET("/code/address/{parentCode}")
    suspend fun getAddressCode(
        @Header("Authorization") Authorization:String,
        @Path("parentCode") parentCode: String?
    ): Response<AddressCodeResponse>

}