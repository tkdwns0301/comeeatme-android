package com.hand.comeeatme.view.main.home.search

import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.home.NicknamesResponse

sealed class SearchState {
    object Uninitialized : SearchState()

    data class Success(
        val nicknames: NicknamesResponse?,
    ) : SearchState()

    data class Error(
        @StringRes val message: String,
    ): SearchState()

    object Loading: SearchState()
}