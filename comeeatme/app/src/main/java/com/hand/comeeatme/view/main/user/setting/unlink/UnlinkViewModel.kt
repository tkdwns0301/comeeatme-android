package com.hand.comeeatme.view.main.user.setting.unlink

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.request.member.ReasonRequest
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class UnlinkViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
) : BaseViewModel() {
    val unlinkStateLiveData = MutableLiveData<UnlinkState>(UnlinkState.Uninitialized)

    private var reason: String? = null

    fun setReason(reason: String) {
        this.reason = reason
    }

    fun getReason(): String? = reason

    fun putUnLinkReason() = viewModelScope.launch {
        unlinkStateLiveData.value = UnlinkState.Loading

        val reasonRequest = ReasonRequest(reason!!)

        val response = memberRepository.putUnlinkReason(
            "${appPreferenceManager.getAccessToken()}",
            reasonRequest
        )

        response?.let {
            unLinkService()
        }

    }

    private fun unLinkService() = viewModelScope.launch {
        unlinkStateLiveData.value = UnlinkState.Loading

        val response = memberRepository.unLinkService(
            "${appPreferenceManager.getAccessToken()}"
        )

        response?.let {
            unlinkStateLiveData.value = UnlinkState.Success
            appPreferenceManager.clear()
        } ?: run {
            unlinkStateLiveData.value = UnlinkState.Error(
                "회원탈퇴를 하는 도중 오류가 발생했습니다. 다시 시도해주세요."
            )
        }
    }

}