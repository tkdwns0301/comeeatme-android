package com.hand.comeeatme.view.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.BuildConfig
import com.hand.comeeatme.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

class LogInActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        Log.e("kakao_api_key", "${BuildConfig.kakao_api_key}")

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Log.e("LogIn Error: ", "접근이 거부 됨(동의 취소)")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Log.e("LogIn Error: ", "유효하지 않은 앱")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Log.e("LogIn Error: ", "인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Log.e("LogIn Error: ", "요청 파라미터 오류")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Log.e("LogIn Error: ", "유효하지 않은 scope ID")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Log.e("LogIn Error: ", "설정이 올바르지 않음(android key hash)")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Log.e("LogIn Error: ", "서버 내부 에러")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Log.e("LogIn Error: ", "앱이 요청 권한이 없음")
                    }
                    else -> {
                        Log.e("LogIn Error: ", "기타 에러: $error")
                    }
                }
            } else if (token != null) {
                Log.e("token", "${token.accessToken}")
            }
        }

        binding.btnLogIn.setOnClickListener{
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
                UserApiClient.instance.loginWithKakaoTalk(applicationContext, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}