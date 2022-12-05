package com.hand.comeeatme.view.login.term

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class TermViewModel(

): BaseViewModel() {
    val termStateLiveData = MutableLiveData<TermState>(TermState.Uninitialized)

    private var isTerm1 = false
    private var isTerm2 = false

    fun setTerm1(term: Boolean) = viewModelScope.launch {
        // TODO 서버로 상태 보내기
        isTerm1 = term

        if(termAllReady()) {
            termStateLiveData.value = TermState.TermAllReady
        } else {
            termStateLiveData.value = TermState.TermNotReady
        }

    }

    fun setTerm2(term: Boolean) = viewModelScope.launch {
        // TODO 서버로 상태 보내기
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

    fun agreeAllTerm() = viewModelScope.launch {
        termStateLiveData.value = TermState.Loading

        termStateLiveData.value = TermState.Success
    }

}