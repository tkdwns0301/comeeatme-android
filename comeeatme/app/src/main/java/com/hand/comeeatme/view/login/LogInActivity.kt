package com.hand.comeeatme.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.data.LogInRequest
import com.hand.comeeatme.data.LogInResponse
import com.hand.comeeatme.databinding.ActivityLoginBinding
import com.hand.comeeatme.network.RetrofitClient
import com.hand.comeeatme.service.OAuthService
import com.hand.comeeatme.view.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LogInActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var retrofit: Retrofit
    private lateinit var oauthService: OAuthService

    private lateinit var callback :(OAuthToken?, Throwable?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        callback = { token, error ->
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
                tokenToServer(token.accessToken)
            }
        }

        initListener()
        initRetrofit()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        oauthService = retrofit.create(OAuthService::class.java)
    }

    private fun tokenToServer(accessToken: String) {
        val kakaoToken = LogInRequest(accessToken)
        val logInService = oauthService.sendTokenToServer("kakao", kakaoToken)

        logInService.enqueue(object: Callback<LogInResponse> {
            override fun onResponse(call: Call<LogInResponse>, response: Response<LogInResponse>) {
                if(response.isSuccessful) {
                    val accessToken = response.body()!!.accessToken
                    val refreshToken = response.body()!!.refreshToken
                    val memberId = response.body()!!.memberId

                    Log.e("Success LogIn: ", "accessToken: $accessToken, refreshToken: $refreshToken, memberId: $memberId")

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "로그인 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun initListener() {
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