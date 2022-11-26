package com.hand.comeeatme.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.logIn.LogInRepository
import com.hand.comeeatme.data.request.aouth.TokenRequest
import com.hand.comeeatme.data.response.logIn.TokenResponse
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val logInRepository: LogInRepository,
) : BaseViewModel() {
    companion object {
        const val LOGIN_KEY = "LogIn"
    }

    val loginStateLiveData = MutableLiveData<LogInState>(LogInState.Uninitialized)

    fun getToken(kakaoAccessToken: String) =
        viewModelScope.launch {
            loginStateLiveData.value = LogInState.Loading
            val kakaoToken = TokenRequest(kakaoAccessToken)
            val comeEatMeToken = logInRepository.getToken(kakaoToken)

            comeEatMeToken?.let {
                saveToken(it)

                loginStateLiveData.value = LogInState.Success(
                    token = it
                )
            } ?: run {
                loginStateLiveData.value = LogInState.Error(
                    "로그인 실패"
                )
            }
        }

    fun saveToken(token: TokenResponse) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.putAccessToken(token.accessToken)
            appPreferenceManager.putRefreshToken(token.refreshToken)
            appPreferenceManager.putMemberId(token.memberId)
            fetchData()
        }
    }

}