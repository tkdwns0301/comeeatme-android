package com.hand.comeeatme.view.main.rank.region

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.request.code.AddressCodeResponse

sealed class RegionState {
    object Uninitialized: RegionState()

    data class Success(
        val response : AddressCodeResponse
    ): RegionState()

    data class Depth2Success(
        val response: AddressCodeResponse
    ): RegionState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ): RegionState()
}