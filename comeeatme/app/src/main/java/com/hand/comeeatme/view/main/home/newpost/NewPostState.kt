package com.hand.comeeatme.view.main.home.newpost

import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.home.NewPostResponse
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantResponse

sealed class NewPostState {
    object Uninitialized : NewPostState()

    data class Success(
        val response: NewPostResponse?,
    ) : NewPostState()

    data class SearchRestaurantSuccess(
        val restaurants: SimpleRestaurantResponse,
    ) : NewPostState()

    data class Error(
        @StringRes val message: String,
    ) : NewPostState()

    object NewPostReady : NewPostState()

    object NewPostUnReady : NewPostState()

    data class CompressPhotoFinish (
        val compressPhotoList: ArrayList<String>,
    ) : NewPostState()


    object Loading : NewPostState()
}