package com.hand.comeeatme.view.main.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
) : BaseViewModel() {

    val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Uninitialized)

    fun getSearchNicknames(nickname: String) {
        viewModelScope.launch {
            searchStateLiveData.value = SearchState.Loading
            val nicknames = memberRepository.getSearchNicknames("${appPreferenceManager.getAccessToken()}", nickname)

            nicknames?.let {
                searchStateLiveData.value = SearchState.Success(
                    nicknames = it
                )
            } ?: run{
                searchStateLiveData.value = SearchState.Error(
                    "검색 실패"
                )
            }
        }
    }

    fun searchNickname(nickname: String) {


    }

}