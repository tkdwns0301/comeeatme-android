package com.hand.comeeatme.data.response.like

import com.google.gson.annotations.Expose
import com.hand.comeeatme.data.response.ErrorResponse

data class SuccessResponse(
    @Expose
    val success: Boolean,
    @Expose
    val error: ErrorResponse?
)

