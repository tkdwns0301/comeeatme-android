package com.hand.comeeatme.view.main.user.setting

import androidx.lifecycle.MutableLiveData
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.view.base.BaseViewModel

class SettingViewModel (
    private val appPreferenceManager: AppPreferenceManager,
): BaseViewModel(){
    val settingStateLiveData = MutableLiveData<SettingState>(SettingState.Uninitialized)


}