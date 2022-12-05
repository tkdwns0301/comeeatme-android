package com.hand.comeeatme.view.main.user.setting

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.widget.Toast
import com.hand.comeeatme.databinding.ActivitySettingBinding
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.login.term.Term1Activity
import com.hand.comeeatme.view.login.term.Term2Activity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : BaseActivity<SettingViewModel, ActivitySettingBinding>(){
    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java)
    }

    override val viewModel by viewModel<SettingViewModel>()
    override fun getViewBinding(): ActivitySettingBinding = ActivitySettingBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.settingStateLiveData.observe(this) {
        when(it) {
            is SettingState.Uninitialized -> {

            }

            is SettingState.Loading -> {

            }

            is SettingState.Success -> {

            }

            is SettingState.Error -> {
                Toast.makeText(applicationContext, "$it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initView() = with(binding) {
        getVersionInfo()

        toolbarSetting.setNavigationOnClickListener {
            finish()
        }

        tbNoti.setOnClickListener {
            if(tbNoti.isChecked) {
                // 알림 켬
            } else {
                // 알림 끔
            }
        }

        clLogOut.setOnClickListener {
            // TODO 로그아웃
        }

        clContact.setOnClickListener {
            startActivity(MailActivity.newIntent(applicationContext))
        }

        clNotice.setOnClickListener {
            // TODO 공지사항
        }

        clTerm1.setOnClickListener {
            startActivity(Term1Activity.newIntent(applicationContext))
        }

        clTerm2.setOnClickListener {
            startActivity(Term2Activity.newIntent(applicationContext))
        }

        tvVersion3.setOnClickListener {
            // TODO 플레이스토어로 연결
        }

        clWithdrawal.setOnClickListener {
            // TODO 회원탈퇴
        }

    }

    private fun getVersionInfo() {
        val info: PackageInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
        val version = info.versionName

        binding.tvVersion2.text = "$version"

    }



}