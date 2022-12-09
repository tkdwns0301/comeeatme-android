package com.hand.comeeatme.data.response.kakao

data class KakaoAddressResponse(
    val meta: Meta,
    val documents: List<Documents>
)

data class Meta (
    val total_count: Long,
)

data class Documents (
    val region_type : String,
    val address_name : String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val region_4depth_name: String,
    val code: String,
    val x: Double,
    val y: Double,
)
