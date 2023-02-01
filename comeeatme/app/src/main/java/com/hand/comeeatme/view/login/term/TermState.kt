package com.hand.comeeatme.view.login.term

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class TermState {
    object Uninitialized: TermState()
    object Loading: TermState()
    object Success: TermState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ):TermState()

    object TermAllReady: TermState()
    object TermNotReady: TermState()
}