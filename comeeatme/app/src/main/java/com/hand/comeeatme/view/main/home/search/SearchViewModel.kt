package com.hand.comeeatme.view.main.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
    private val restaurantRepository: RestaurantRepository,
) : BaseViewModel() {

    val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Uninitialized)

    fun getSearchNicknames(nickname: String) {
        viewModelScope.launch {
            searchStateLiveData.value = SearchState.Loading
            val nicknames = memberRepository.getSearchNicknames("${appPreferenceManager.getAccessToken()}", nickname)

            nicknames?.let {
                searchStateLiveData.value = SearchState.SearchUserSuccess(
                    response = it
                )
            } ?: run{
                searchStateLiveData.value = SearchState.Error(
                    "검색 실패"
                )
            }
        }
    }

    fun getSearchRestaurants(
        page: Long?,
        size: Long?,
        sort: Boolean?,
        keyword: String,
    ) = viewModelScope.launch {
        searchStateLiveData.value = SearchState.Loading

        val response = restaurantRepository.getSearchRestaurants(
            "${appPreferenceManager.getAccessToken()}",
            page,
            size,
            sort,
            keyword
        )

        response?.let {
            searchStateLiveData.value = SearchState.SearchRestaurantSuccess(
                response = it
            )
        }?:run {
            searchStateLiveData.value = SearchState.Error(
                "검색한 단어의 식당을 찾을 수 없습니다."
            )
        }
    }



}