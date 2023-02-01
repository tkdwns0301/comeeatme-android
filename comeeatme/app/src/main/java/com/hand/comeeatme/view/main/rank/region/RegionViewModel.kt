package com.hand.comeeatme.view.main.rank.region

import android.widget.RadioButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.code.CodeRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class RegionViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val codeRepository: CodeRepository,
) : BaseViewModel() {
    val regionStateLiveData = MutableLiveData<RegionState>(RegionState.Uninitialized)

    private var depth1: String? = null
    private var depth2: String? = null
    private var addCode: String? = null
    private var depth1RadioButtonList = arrayListOf<RadioButton>()
    private var depth2RadioButtonList = arrayListOf<RadioButton>()

    fun setDepth1(depth1: String) {
        this.depth1 = depth1
    }
    fun getDepth1(): String = depth1!!

    fun setDepth2(depth2: String) {
        this.depth2 = depth2
    }
    fun getDepth2(): String = depth2!!

    fun setAddCode(addCode: String) {
        this.addCode = addCode
    }
    fun getAddCode(): String = addCode!!

    fun addDepth1RadioButtonList(radioButton: RadioButton) {
        this.depth1RadioButtonList.add(radioButton)
    }
    fun getDepth1RadioButtonList() : ArrayList<RadioButton> = depth1RadioButtonList

    fun addDepth2RadioButtonList(radioButton: RadioButton) {
        this.depth2RadioButtonList.add(radioButton)
    }
    fun getDepth2RadioButtonList() : ArrayList<RadioButton> = depth2RadioButtonList


    fun initDepth2RadioButtonList() {
        depth2RadioButtonList = arrayListOf<RadioButton>()
    }

    fun getAddressCode(parentCode: String?, isDepth1: Boolean) = viewModelScope.launch {
        val response = codeRepository.getAddressCode(
            "${appPreferenceManager.getAccessToken()}",
            parentCode = parentCode
        )


        response?.let {
            if (isDepth1) {
                regionStateLiveData.value = RegionState.Success(
                    response = it
                )
            } else {
                regionStateLiveData.value = RegionState.Depth2Success(
                    response = it
                )
            }

        } ?: run {
            regionStateLiveData.value = RegionState.Error(
                "지역 정보를 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

}