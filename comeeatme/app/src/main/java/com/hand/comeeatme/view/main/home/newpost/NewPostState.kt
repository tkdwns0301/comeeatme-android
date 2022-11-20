package com.hand.comeeatme.view.main.home.newpost

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.post.NewPostResponse
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantResponse

sealed class NewPostState {
    object Uninitialized : NewPostState()

    data class Success(
        val response: NewPostResponse?,
    ) : NewPostState()

    data class SearchRestaurantSuccess(
        val restaurants: SimpleRestaurantResponse,
    ) : NewPostState()

    @SuppressLint("SupportAnnotationUsage")
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