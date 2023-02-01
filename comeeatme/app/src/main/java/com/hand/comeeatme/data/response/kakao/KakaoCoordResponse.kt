package com.hand.comeeatme.data.response.kakao

import com.google.gson.annotations.Expose

data class KakaoCoordResponse(
    @Expose
    val meta: KakaoCoordMeta,
    @Expose
    val documents: List<KakaoCoordDocuments>
)

data class KakaoCoordMeta(
    @Expose
    val total_count: Long,
    @Expose
    val pageable_count: Long,
    @Expose
    val is_end: Boolean,
)

data class KakaoCoordDocuments(
    @Expose
    val address_name: String,
    @Expose
    val y: String,
    @Expose
    val x: String,
    @Expose
    val address_type: String,
    @Expose
    val address: KakaoCoordAddress,
    @Expose
    val road_address: KakaoCoordRoadAddress,
)

data class KakaoCoordAddress(
    @Expose
    val address_name: String,
    @Expose
    val region_1depth_name: String,
    @Expose
    val region_2depth_name: String,
    @Expose
    val region_3depth_name: String,
    @Expose
    val region_3depth_h_name: String,
    @Expose
    val h_code: String,
    @Expose
    val b_code: String,
    @Expose
    val mountain_yn: String,
    @Expose
    val main_address_no: String,
    @Expose
    val sub_address_no: String,
    @Expose
    val x: String,
    @Expose
    val y: String,
)

data class KakaoCoordRoadAddress(
    @Expose
    val address_name: String,
    @Expose
    val region_1depth_name: String,
    @Expose
    val region_2depth_name: String,
    @Expose
    val region_3depth_name: String,
    @Expose
    val road_name: String,
    @Expose
    val underground_yn: String,
    @Expose
    val main_building_no: String,
    @Expose
    val sub_building_no: String,
    @Expose
    val building_name: String,
    @Expose
    val zone_no: String,
    @Expose
    val y: String,
    @Expose
    val x: String,
)