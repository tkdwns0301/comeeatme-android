package com.hand.comeeatme.data.request.member

data class MemberTermRequest(
    val agreeOrNot: AgreeOrNot,
)

data class AgreeOrNot(
    val PERSONAL_INFORMATION: Boolean,
    val TERMS_OF_SERVICE: Boolean,
)
