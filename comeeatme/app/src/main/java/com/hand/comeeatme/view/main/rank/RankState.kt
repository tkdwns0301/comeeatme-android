package com.hand.comeeatme.view.main.rank

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.request.code.AddressCodeResponse
import com.hand.comeeatme.data.response.restaurant.RestaurantsRankResponse

sealed class RankState {
    object Uninitialized : RankState()
    object Loading : RankState()

    data class Success(
        val response: RestaurantsRankResponse,
    ) : RankState()

    data class CurrentAddressSuccess(
        val depth1: String,
        val depth2: String,
        val addressCode: String,
    ) : RankState()

    data class AddressCodeSuccess(
        val response: AddressCodeResponse
    ): RankState()


    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : RankState()
}