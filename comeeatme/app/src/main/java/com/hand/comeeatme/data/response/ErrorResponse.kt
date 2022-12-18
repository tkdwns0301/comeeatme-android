package com.hand.comeeatme.data.response

import com.google.gson.annotations.Expose

data class ErrorResponse(
    @Expose
    val code: String,
    @Expose
    val message: String,
    @Expose
    val errors: List<String>
)
