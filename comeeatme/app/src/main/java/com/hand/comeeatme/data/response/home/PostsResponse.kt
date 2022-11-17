package com.hand.comeeatme.data.response.home

import com.google.gson.annotations.Expose

data class PostResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: PostData,
)

data class PostData(
    @Expose
    var content: List<Content>,
    @Expose
    var pageable: Pageable,
    @Expose
    var sort: Sort,
    @Expose
    var numberOfElement: Int,
    @Expose
    var first: Boolean,
    @Expose
    var last: Boolean,
    @Expose
    var size: Int,
    @Expose
    var number: Int,
    @Expose
    var empty: Boolean,
)

data class Content(
    @Expose
    var id: Long,
    @Expose
    var imageUrls: ArrayList<String>,
    @Expose
    var content: String,
    @Expose
    var createdAt: String,
    @Expose
    var commentCount: Int,
    @Expose
    var likeCount: Int,
    @Expose
    var member: Member,
    @Expose
    var restaurant: Restaurant,
)

data class Member(
    @Expose
    var id: Long,
    @Expose
    var nickname: String,
    @Expose
    var imageUrl: String,
)

data class Restaurant(
    @Expose
    var id: Long,
    @Expose
    var name: String,

    )

data class Pageable(
    @Expose
    var sort: Sort,
    @Expose
    var pageNumber: Int,
    @Expose
    var offset: Int,
    @Expose
    var paged: Boolean,
    @Expose
    var unpaged: Boolean,
)

data class Sort(
    @Expose
    val sorted: Boolean,
    @Expose
    val unsorted: Boolean,
    @Expose
    val empty: Boolean,
)