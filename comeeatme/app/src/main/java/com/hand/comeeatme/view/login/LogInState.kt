package com.hand.comeeatme.view.login

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.hand.comeeatme.data.response.logIn.TokenResponse

sealed class LogInState {
    object Uninitialized : LogInState()

    data class Success(
        val token: TokenResponse,
    ) : LogInState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ) : LogInState()

    object Loading : LogInState()


}