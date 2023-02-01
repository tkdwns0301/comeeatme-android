package com.hand.comeeatme.data.repository.notice

import com.hand.comeeatme.data.response.notice.NoticesResponse

interface NoticeRepository {
    suspend fun getNotices(accessToken: String, page: Long, size: Long) : NoticesResponse?

}