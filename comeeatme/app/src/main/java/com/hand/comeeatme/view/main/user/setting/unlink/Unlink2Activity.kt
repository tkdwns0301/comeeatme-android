package com.hand.comeeatme.view.main.user.setting.unlink

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.hand.comeeatme.databinding.ActivityUnlink2Binding
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.dialog.UnlinkDialog
import com.hand.comeeatme.view.login.LogInActivity
import com.kakao.sdk.user.UserApiClient
import org.koin.androidx.viewmodel.ext.android.viewModel

class Unlink2Activity: BaseActivity<UnlinkViewModel, ActivityUnlink2Binding>() {
    companion object {
        fun newIntent(context: Context) = Intent(context, Unlink2Activity::class.java)
    }

    override val viewModel by viewModel<UnlinkViewModel>()
    override fun getViewBinding(): ActivityUnlink2Binding = ActivityUnlink2Binding.inflate(layoutInflater)

    override fun observeData() = viewModel.unlinkStateLiveData.observe(this) {
        when(it) {
            is UnlinkState.Uninitialized -> {

            }

            is UnlinkState.Loading -> {

            }

            is UnlinkState.Success -> {
                UserApiClient.instance.unlink { error ->
                    if(error != null) {
                        Toast.makeText(applicationContext, "오류로 인해 회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(applicationContext, LogInActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

                        startActivity(intent)
                    }
                }
            }

            is UnlinkState.Error -> {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initView() = with(binding) {
        clUnlink.setOnClickListener {
            UnlinkDialog(
                this@Unlink2Activity,
                unLink = {
                    viewModel.withdrawalService()
                }
            ).show()
        }

        toolbarUnlink.setNavigationOnClickListener {
            finish()
        }
    }
}