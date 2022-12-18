package com.hand.comeeatme.view.main.home

import com.hand.comeeatme.data.response.post.Content

sealed class HomeState {
    object Uninitialized: HomeState()
    object Loading: HomeState()

    data class Success(
        val posts: ArrayList<Content>
    ): HomeState()

    object LikePostSuccess: HomeState()
    object UnLikePostSuccess: HomeState()

    object BookmarkPostSuccess: HomeState()
    object UnBookmarkPostSuccess: HomeState()

    data class Error(
        val message: String
    ): HomeState()

}