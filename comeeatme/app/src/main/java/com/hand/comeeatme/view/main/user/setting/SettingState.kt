package com.hand.comeeatme.view.main.user.setting

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class SettingState {
    object Uninitialized : SettingState()
    object Loading: SettingState()
    object Success: SettingState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ):SettingState()
}