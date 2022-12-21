package com.hand.comeeatme.view.main.user.setting.unlink

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class UnlinkState {
    object Uninitialized: UnlinkState()
    object Success: UnlinkState()
    object Loading: UnlinkState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String
    ): UnlinkState()

}