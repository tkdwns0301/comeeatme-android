package com.hand.comeeatme.view.login

import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.hand.comeeatme.databinding.ActivityLoginBinding
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import org.koin.androidx.viewmodel.ext.android.viewModel

class LogInActivity : BaseActivity<LogInViewModel, ActivityLoginBinding>() {

    override val viewModel by viewModel<LogInViewModel>()
    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    private lateinit var callback :(OAuthToken?, Throwable?) -> Unit

    override fun observeData() = viewModel.loginStateLiveData.observe(this) {
        when (it) {
            is LogInState.Uninitialized -> {
                binding.pbLoading.isGone = true
            }

            is LogInState.Loading -> {
                binding.pbLoading.isVisible = true
            }

            is LogInState.Success -> {
                binding.pbLoading.isGone = true
                startActivity(
                    MainActivity.newIntent(applicationContext)
                )
                finish()
            }

            is LogInState.Error -> {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun initView() = with(binding) {
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
                viewModel.getToken(token.accessToken)
            }
        }

        btnLogIn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
                UserApiClient.instance.loginWithKakaoTalk(applicationContext, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(applicationContext,
                    callback = callback)
            }
        }

    }
}