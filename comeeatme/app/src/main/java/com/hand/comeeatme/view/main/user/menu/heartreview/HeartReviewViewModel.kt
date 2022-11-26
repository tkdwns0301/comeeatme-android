package com.hand.comeeatme.view.main.user.menu.heartreview

import androidx.lifecycle.MutableLiveData
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.view.base.BaseViewModel

class HeartReviewViewModel(
    appPreferenceManager: AppPreferenceManager,
) : BaseViewModel() {
    val heartReviewStateLiveData = MutableLiveData<HeartReviewState>(HeartReviewState.Uninitialized)


}