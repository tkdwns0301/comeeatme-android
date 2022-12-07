package com.hand.comeeatme.data.request.code

data class AddressCodeResponse(
    val success: Boolean,
    val data: List<AddressCodeData>
)

data class AddressCodeData(
    val code: String,
    val name: String,
    val terminal: Boolean,
)

