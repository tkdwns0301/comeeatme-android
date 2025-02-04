package com.hand.comeeatme.view.main.home.search

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.member.MemberSearchResponse
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent

sealed class SearchState {
    object Uninitialized : SearchState()

    data class SearchUserSuccess(
        val response: MemberSearchResponse?,
    ) : SearchState()

    data class SearchRestaurantSuccess(
        val response: ArrayList<SimpleRestaurantContent>
    ) : SearchState()


    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : SearchState()

    object Loading : SearchState()
}