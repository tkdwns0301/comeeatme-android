package com.hand.comeeatme.data.repository.code

import com.hand.comeeatme.data.network.CodeService
import com.hand.comeeatme.data.request.code.AddressCodeResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultCodeRepository(
    private val codeService: CodeService,
    private val ioDispatcher: CoroutineDispatcher,
) : CodeRepository {
    override suspend fun getAddressCode(
        accessToken: String,
        parentCode: String?,
    ): AddressCodeResponse? = withContext(ioDispatcher) {
        val response = codeService.getAddressCode(
            "Bearer $accessToken",
            parentCode
        )

        if (response.isSuccessful) {
            if (!response.body()!!.success) {
                null
            } else {
                response.body()!!
            }
        } else {
            null
        }
    }
}