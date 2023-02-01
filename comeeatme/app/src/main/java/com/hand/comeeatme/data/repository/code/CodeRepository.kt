package com.hand.comeeatme.data.repository.code

import com.hand.comeeatme.data.request.code.AddressCodeResponse

interface CodeRepository {
    suspend fun getAddressCode(accessToken: String, parentCode: String?): AddressCodeResponse?

}