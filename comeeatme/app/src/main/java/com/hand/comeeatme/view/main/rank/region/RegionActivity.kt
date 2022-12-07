package com.hand.comeeatme.view.main.rank.region

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import com.hand.comeeatme.R
import com.hand.comeeatme.data.request.code.AddressCodeData
import com.hand.comeeatme.databinding.ActivityRegionBinding
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.main.rank.RankFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegionActivity: BaseActivity<RegionViewModel, ActivityRegionBinding>() {
    companion object{
        const val DEPTH1 = "depth1"
        const val DEPTH2= "depth2"
        const val ADDRESS_CODE = "addressCode"

        fun newIntent(context: Context, depth1: String, depth2: String, addressCode: String) = Intent(context, RegionActivity::class.java).apply {
            putExtra(DEPTH1, depth1)
            putExtra(DEPTH2, depth2)
            putExtra(ADDRESS_CODE, addressCode)
        }
    }



    override val viewModel by viewModel<RegionViewModel>()
    override fun getViewBinding(): ActivityRegionBinding = ActivityRegionBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.regionStateLiveData.observe(this){
        when(it) {
            is RegionState.Uninitialized -> {
                viewModel.setDepth1(intent.getStringExtra(DEPTH1)!!)
                viewModel.setDepth2(intent.getStringExtra(DEPTH2)!!)
                viewModel.setAddCode(intent.getStringExtra(ADDRESS_CODE)!!)

                viewModel.getAddressCode("", true)
            }
            is RegionState.Success -> {
                setDepth1(it.response.data)
            }

            is RegionState.Depth2Success -> {
                setDepth2(it.response.data)
            }

            is RegionState.Error -> {

            }
        }

    }

    override fun initView() = with(binding){
        toolbarRegion.setNavigationOnClickListener {
            val intent = Intent(applicationContext, RankFragment::class.java)
            intent.putExtra(DEPTH1, viewModel.getDepth1())
            intent.putExtra(DEPTH2, viewModel.getDepth2())
            intent.putExtra(ADDRESS_CODE, viewModel.getAddCode())
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun setDepth1(datas: List<AddressCodeData>) {
        datas.forEach { data ->
            Log.e("data", "add?")
            binding.rgDepth1.addItem(data.name, data.code, true)
        }

        viewModel.getDepth1RadioButtonList().forEach { radioButton ->
            if(radioButton.text == viewModel.getDepth1()) {
                radioButton.isChecked = true
            }
        }
    }

    private fun setDepth2(datas: List<AddressCodeData>) {
        datas.forEach { data ->
            binding.rgDepth2.addItem(data.name, data.code, false )
        }

        viewModel.getDepth2RadioButtonList().forEach { radioButton ->
            if(radioButton.text == viewModel.getDepth2()) {
                radioButton.isChecked = true
            }
        }
    }

    private fun RadioGroup.addItem(region: String, addressCode: String, isDepth1: Boolean) {
        val radioButton: RadioButton = LayoutInflater.from(context)
            .inflate(R.layout.layout_radio_button_custom, null) as RadioButton

        radioButton.apply {
            text = region
            hint = addressCode

            if(isDepth1) {
                viewModel.addDepth1RadioButtonList(this)
            } else {
                viewModel.addDepth2RadioButtonList(this)
            }

            setOnCheckedChangeListener {_, isChecked ->
                if(isChecked) {
                    if(isDepth1) {
                        viewModel.setDepth1("$text")
                        binding.rgDepth2.removeAllViews()
                        viewModel.getAddressCode("$hint", false)
                    } else {
                        viewModel.initDepth2RadioButtonList()
                        viewModel.setDepth2("$text")
                        viewModel.setAddCode("$hint")
                    }
                }
            }


        }

        addView(radioButton, childCount, layoutParams)
    }
}