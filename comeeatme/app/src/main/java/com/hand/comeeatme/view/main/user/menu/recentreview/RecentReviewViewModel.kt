package com.hand.comeeatme.view.main.user.menu.recentreview

import androidx.lifecycle.MutableLiveData
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.view.base.BaseViewModel

class RecentReviewViewModel(
    private val appPreferenceManager: AppPreferenceManager
): BaseViewModel() {
    val recentReviewStateLiveData = MutableLiveData<RecentReviewState>(RecentReviewState.Uninitialized)



}
