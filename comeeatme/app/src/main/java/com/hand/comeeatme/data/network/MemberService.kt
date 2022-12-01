package com.hand.comeeatme.data.network

import com.hand.comeeatme.data.response.home.NicknamesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MemberService {
    //
    @GET("v1/members")
    suspend fun getSearchNicknames(
        @Header("Authorization") Authorization: String,
        @Query("nickname") nickname: String,
    ): Response<NicknamesResponse>


}