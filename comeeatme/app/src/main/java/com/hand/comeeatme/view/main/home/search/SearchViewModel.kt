package com.hand.comeeatme.view.main.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
    private val restaurantRepository: RestaurantRepository,
) : BaseViewModel() {

    val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Uninitialized)

    private var page: Long = 0
    private var contents = arrayListOf<SimpleRestaurantContent>()
    private var isLast: Boolean = false
    private var query: String = ""
    private var type: Boolean = false

    fun getType(): Boolean = type
    fun setType(type: Boolean) {
        this.type = type
    }

    fun setQuery(query: String) {
        this.query = query
    }

    fun getIsLast(): Boolean = isLast
    fun setIsLast(isLast: Boolean) {
        this.isLast = isLast
    }

    fun getSearchNicknames() {
        viewModelScope.launch {
            searchStateLiveData.value = SearchState.Loading
            val nicknames = memberRepository.getSearchNicknames("${appPreferenceManager.getAccessToken()}", query)

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
        isRefresh: Boolean,
    ) = viewModelScope.launch {
        searchStateLiveData.value = SearchState.Loading

        if(isRefresh) {
            page = 0
            contents = arrayListOf()
        }

        val response = restaurantRepository.getSearchRestaurants(
            "${appPreferenceManager.getAccessToken()}",
            page++,
            10,
            query
        )

        response?.let {
            if(it.data!!.content.isNotEmpty()) {
                contents.addAll(it.data.content)

                searchStateLiveData.value = SearchState.SearchRestaurantSuccess(
                    response = contents
                )

                isLast = false
            } else {
                isLast = true

                searchStateLiveData.value = SearchState.SearchRestaurantSuccess(
                    response = contents
                )
            }


        }?:run {
            searchStateLiveData.value = SearchState.Error(
                "검색한 단어의 식당을 찾을 수 없습니다."
            )
        }
    }



}