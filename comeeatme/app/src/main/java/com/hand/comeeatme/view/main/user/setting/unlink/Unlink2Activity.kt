package com.hand.comeeatme.view.main.user.setting.unlink

import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityUnlink2Binding
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.dialog.UnlinkDialog
import com.hand.comeeatme.view.login.LogInActivity
import com.kakao.sdk.user.UserApiClient
import org.koin.androidx.viewmodel.ext.android.viewModel

class Unlink2Activity : BaseActivity<UnlinkViewModel, ActivityUnlink2Binding>() {
    companion object {
        fun newIntent(context: Context) = Intent(context, Unlink2Activity::class.java)
    }

    override val viewModel by viewModel<UnlinkViewModel>()
    override fun getViewBinding(): ActivityUnlink2Binding =
        ActivityUnlink2Binding.inflate(layoutInflater)

    override fun observeData() = viewModel.unlinkStateLiveData.observe(this) {
        when (it) {
            is UnlinkState.Uninitialized -> {

            }

            is UnlinkState.Loading -> {
                binding.clLoading.isVisible = true
                window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            is UnlinkState.Success -> {
                binding.clLoading.isGone = true
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        Toast.makeText(applicationContext,
                            "오류로 인해 회원탈퇴에 실패하였습니다.",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT)
                            .show()

                        val intent = Intent(applicationContext, LogInActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

                        startActivity(intent)
                    }
                }
            }

            is UnlinkState.Error -> {
                binding.clLoading.isGone = true
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initView() = with(binding) {
        Glide.with(applicationContext)
            .load(R.drawable.loading)
            .into(ivLoading)

        clUnlink.setOnClickListener {
            if(viewModel.getReason() == null) {
                Toast.makeText(applicationContext, "사유를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                UnlinkDialog(
                    this@Unlink2Activity,
                    unLink = {
                        viewModel.putUnLinkReason()
                    }
                ).show()
            }
        }

        toolbarUnlink.setNavigationOnClickListener {
            finish()
        }

        rgUnlinkReason.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rv_Reason1 -> {
                    viewModel.setReason("FREQUENT_ERROR")
                }
                R.id.rv_Reason2 -> {
                    viewModel.setReason("NO_INFORMATION")
                }
                R.id.rv_Reason3 -> {
                    viewModel.setReason("DELETE_PERSONAL_INFORMATION")
                }
                R.id.rv_Reason4 -> {
                    viewModel.setReason("LOW_VISIT_FREQUENCY")
                }
            }
        }
    }
}