package com.hand.comeeatme.view.login.term

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.request.member.AgreeOrNot
import com.hand.comeeatme.data.request.member.MemberTermRequest
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class TermViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
): BaseViewModel() {
    val termStateLiveData = MutableLiveData<TermState>(TermState.Uninitialized)

    private var isTerm1 = false
    private var isTerm2 = false

    fun setTerm1(term: Boolean) = viewModelScope.launch {
        isTerm1 = term

        if(termAllReady()) {
            termStateLiveData.value = TermState.TermAllReady
        } else {
            termStateLiveData.value = TermState.TermNotReady
        }

    }

    fun setTerm2(term: Boolean) = viewModelScope.launch {
        isTerm2 = term

        if(termAllReady()) {
            termStateLiveData.value = TermState.TermAllReady
        } else {
            termStateLiveData.value = TermState.TermNotReady
        }
    }

    private fun termAllReady() : Boolean {
        return isTerm1 && isTerm2
    }

    fun setTermsAgree() = viewModelScope.launch {
        termStateLiveData.value = TermState.Loading

        val agreeOrNot = MemberTermRequest(AgreeOrNot(isTerm1, isTerm2))

        val response = memberRepository.setTermsAgree(
            "${appPreferenceManager.getAccessToken()}",
            agreeOrNot
        )

        response?.let {
            termStateLiveData.value = TermState.Success
            appPreferenceManager.putMemberId(it.data.id)
        }?: run {
            termStateLiveData.value = TermState.Error(
                "약관동의를 하는 도중 오류가 발생했습니다."
            )
        }
    }

}