package com.hand.comeeatme

import android.app.Application
import android.content.Context
import com.hand.comeeatme.di.appModule
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ComeEatMeApplication : Application() {
    companion object {
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "${BuildConfig.kakao_api_key}")

        appContext = this

        startKoin {
            androidLogger()
            androidContext(this@ComeEatMeApplication)
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }



}