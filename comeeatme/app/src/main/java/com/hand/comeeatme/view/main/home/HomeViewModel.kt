package com.hand.comeeatme.view.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.home.PostRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val postRepository: PostRepository
): BaseViewModel() {
    companion object{
        const val COMMUNITY_KEY = "Community"
    }

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun loadPost(
        page: Int?,
        size: Int?,
        sort: Boolean?,
        hashTags: List<String>?
    ) =
        viewModelScope.launch {
            homeStateLiveData.value = HomeState.Loading

            val posts = postRepository.getPosts("${appPreferenceManager.getAccessToken()}", page, size, sort, hashTags )

            posts?.let { post ->
                homeStateLiveData.value = HomeState.Success(
                    posts = post
                )
            }?: run {
                homeStateLiveData.value = HomeState.Error("데이터 없음")
            }
        }

}