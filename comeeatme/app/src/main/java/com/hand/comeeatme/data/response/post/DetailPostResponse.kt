package com.hand.comeeatme.data.response.post


data class DetailPostResponse(
    val success: Boolean,
    val data: DetailPostData,
)

data class DetailPostData(
    val id: Long,
    val imageUrls: List<String>,
    val content: String,
    val hashtags: List<String>,
    val createdAt: String,
    val commentCount: Int,
    val likeCount: Int,
    val liked: Boolean,
    val bookmarked: Boolean,
    val member: Member,
    val restaurant: DetailRestaurant,
)

data class DetailRestaurant(
    val id: Long,
    val name: String,
    val address: Point,
)

data class Point(
    val name: String,
    val x: Double,
    val y: Double,
)
