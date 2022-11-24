package com.hand.comeeatme.view.main.bookmark

import androidx.lifecycle.MutableLiveData
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.view.base.BaseViewModel

class BookmarkViewModel(
    private val appPreferenceManager: AppPreferenceManager,
) : BaseViewModel() {
    val bookmarkStateLiveData = MutableLiveData<BookmarkState>(BookmarkState.Uninitialized)


}