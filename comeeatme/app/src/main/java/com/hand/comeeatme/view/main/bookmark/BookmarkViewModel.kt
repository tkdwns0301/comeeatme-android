package com.hand.comeeatme.view.main.bookmark

import androidx.lifecycle.MutableLiveData
import com.hand.comeeatme.view.base.BaseViewModel

class BookmarkViewModel(
) : BaseViewModel() {
    val bookmarkStateLiveData = MutableLiveData<BookmarkState>(BookmarkState.Uninitialized)


}