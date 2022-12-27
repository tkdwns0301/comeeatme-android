package com.hand.comeeatme.data.repository.notice

import com.hand.comeeatme.data.network.NoticeService
import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.response.notice.NoticesResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultNoticeRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val oAuthService: OAuthService,
    private val noticeService: NoticeService,
    private val ioDispatcher: CoroutineDispatcher,
) : NoticeRepository {
    override suspend fun getNotices(
        accessToken: String,
        page: Long,
        size: Long,
    ): NoticesResponse? = withContext(ioDispatcher) {
        val response = noticeService.getNotices(
            Authorization = "Bearer $accessToken",
            page = page,
            size = size,
        )

        if (response.isSuccessful) {
            response.body()!!
        } else if (response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}"
            )

            if (response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                getNotices(
                    "${appPreferenceManager.getAccessToken()}",
                    page,
                    size)
            } else {
                null
            }
        } else {
            null
        }
    }
}