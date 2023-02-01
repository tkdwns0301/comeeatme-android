package com.hand.comeeatme.view.main.home.post.report

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class ReportState {
    object Uninitialized: ReportState()
    object Loading: ReportState()
    object Success: ReportState()

    @SuppressLint("SupportAnnotationUsage")
    data class Error(
        @StringRes val message: String,
    ): ReportState()
}