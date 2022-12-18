package com.hand.comeeatme.view.main.user.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel (
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
): BaseViewModel(){
    val settingStateLiveData = MutableLiveData<SettingState>(SettingState.Uninitialized)


    fun withdrawalService() = viewModelScope.launch {
        settingStateLiveData.value = SettingState.Loading

        val response = memberRepository.withdrawalService(
            "${appPreferenceManager.getAccessToken()}"
        )

        response?.let {
            settingStateLiveData.value = SettingState.Success
            appPreferenceManager.clear()

        }?:run {
            settingStateLiveData.value = SettingState.Error(
                "회원탈퇴를 하는 도중 오류가 발생했습니다. 다시 시도해주세요."
            )
        }
    }
}