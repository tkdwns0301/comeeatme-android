package com.hand.comeeatme.data.repository.report

import com.hand.comeeatme.data.network.OAuthService
import com.hand.comeeatme.data.network.ReportService
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.request.report.ReportRequest
import com.hand.comeeatme.data.response.member.MemberModifyResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultReportRepository(
    private val appPreferenceManager: AppPreferenceManager,
    private val oAuthService: OAuthService,
    private val reportService: ReportService,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportRepository {
    override suspend fun reportPost(
        accessToken: String,
        postId: Long,
        reason: ReportRequest,
    ): MemberModifyResponse? = withContext(ioDispatcher) {
        val response = reportService.reportPost(
            Authorization = "Bearer $accessToken",
            postId = postId,
            reason = reason
        )

        if (response.isSuccessful) {
            response.body()!!
        } else if(response.code() == 401) {
            val response2 = oAuthService.reissueToken(
                "Bearer ${appPreferenceManager.getRefreshToken()}",
            )

            if(response2.isSuccessful) {
                appPreferenceManager.putRefreshToken(response2.body()!!.refreshToken)
                appPreferenceManager.putAccessToken(response2.body()!!.accessToken)

                reportPost(
                    "${appPreferenceManager.getAccessToken()}",
                    postId,
                    reason
                )
            } else {
                null
            }
        } else {
            null
        }

    }
}