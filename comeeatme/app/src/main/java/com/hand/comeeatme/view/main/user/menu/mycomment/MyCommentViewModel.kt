package com.hand.comeeatme.view.main.user.menu.mycomment

import androidx.lifecycle.MutableLiveData
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.view.base.BaseViewModel

class MyCommentViewModel(
    appPreferenceManager: AppPreferenceManager
): BaseViewModel() {
    val myCommentStateLiveData = MutableLiveData<MyCommentState>(MyCommentState.Uninitialized)
}