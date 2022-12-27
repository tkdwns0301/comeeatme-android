package com.hand.comeeatme.view.main.user.setting

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.widget.Toast
import com.hand.comeeatme.databinding.ActivitySettingBinding
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.dialog.LogoutDialog
import com.hand.comeeatme.view.login.LogInActivity
import com.hand.comeeatme.view.login.onboarding.OnBoardingActivity
import com.hand.comeeatme.view.login.term.Term1Activity
import com.hand.comeeatme.view.login.term.Term2Activity
import com.hand.comeeatme.view.main.user.setting.notice.NoticeActivity
import com.hand.comeeatme.view.main.user.setting.unlink.Unlink1Activity
import com.kakao.sdk.user.UserApiClient
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

            is SettingState.Logout -> {
                UserApiClient.instance.logout { error ->
                    if(error != null) {
                        Toast.makeText(applicationContext, "카카오 오류로 인해 로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(applicationContext, LogInActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
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

        clLogOut.setOnClickListener {
            LogoutDialog(
                this@SettingActivity,
                logout = {
                    viewModel.logout()
                }
            ).show()
        }

        clContact.setOnClickListener {
            startActivity(MailActivity.newIntent(applicationContext))
        }

        clNotice.setOnClickListener {
            startActivity(NoticeActivity.newIntent(applicationContext))
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

        clVersion.setOnClickListener {
            startActivity(OnBoardingActivity.newIntent(applicationContext))
        }

        clWithdrawal.setOnClickListener {
            startActivity(Unlink1Activity.newIntent(applicationContext))
        }

        tbNoti.setOnClickListener {
            if(tbNoti.isChecked) {
                Toast.makeText(applicationContext, "현재 사용할 수 없는 기능이에요.\n빠른 시일 내에 업데이트하겠습니다.", Toast.LENGTH_SHORT).show()
                tbNoti.isChecked = false
            }
        }

    }

    private fun getVersionInfo() {
        val info: PackageInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
        val version = info.versionName

        binding.tvVersion2.text = "$version"

    }



}