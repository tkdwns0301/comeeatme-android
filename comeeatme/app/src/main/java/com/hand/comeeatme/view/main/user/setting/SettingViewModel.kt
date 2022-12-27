package com.hand.comeeatme.view.main.user.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.repository.oauth.OAuthRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel (
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
    private val oAuthRepository: OAuthRepository
): BaseViewModel(){
    val settingStateLiveData = MutableLiveData<SettingState>(SettingState.Uninitialized)


    fun logout() = viewModelScope.launch {
        settingStateLiveData.value = SettingState.Loading

        val response = oAuthRepository.logout(
            "${appPreferenceManager.getAccessToken()}"
        )

        response?.let {
            settingStateLiveData.value = SettingState.Logout
        }?:run {
            settingStateLiveData.value = SettingState.Error(
                "로그아웃을 하는 도중 오류가 발생했습니다."
            )
        }
    }
}