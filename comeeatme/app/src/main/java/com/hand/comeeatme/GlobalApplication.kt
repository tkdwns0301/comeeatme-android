package com.hand.comeeatme

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "${BuildConfig.kakao_api_key}")
    }

}