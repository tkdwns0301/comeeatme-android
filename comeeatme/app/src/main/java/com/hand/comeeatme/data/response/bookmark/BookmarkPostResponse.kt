package com.hand.comeeatme.data.response.bookmark

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.ErrorResponse
import com.hand.comeeatme.data.response.post.Member
import com.hand.comeeatme.data.response.post.Pageable
import com.hand.comeeatme.data.response.post.Restaurant
import com.hand.comeeatme.data.response.post.Sort

data class BookmarkPostResponse(
    @Expose
    val success: Boolean,
    @Expose
    val data: BookmarkPostData?,
    @Expose
    val error: ErrorResponse?,
)

data class BookmarkPostData(
    @Expose
    val content: List<BookmarkPostContent>,
    @Expose
    val pageable: Pageable,
    @Expose
    val sort: Sort,
    @Expose
    val numberOfElement: Long,
    @Expose
    val first: Boolean,
    @Expose
    val last: Boolean,
    @Expose
    val size: Long,
    @Expose
    val number: Long,
    @Expose
    val empty: Boolean,
)

data class BookmarkPostContent(
    @Expose
    val id: Long,
    @Expose
    val imageUrls: List<String>,
    @Expose
    val content: String,
    @Expose
    val createAt: String,
    @Expose
    val member: Member,
    @Expose
    val restaurant: Restaurant,
    @Expose
    val liked: Boolean,
    @Expose
    val bookmarked: Boolean,
)
