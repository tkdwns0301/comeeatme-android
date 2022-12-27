package com.hand.comeeatme.view.main.home.post.report

import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityReportBinding
import com.hand.comeeatme.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReportActivity : BaseActivity<ReportViewModel, ActivityReportBinding>() {
    companion object {
        const val POST_ID = "postId"
        fun newIntent(context: Context, postId: Long) =
            Intent(context, ReportActivity::class.java).putExtra(POST_ID, postId)
    }

    private val postId by lazy {
        intent.getLongExtra(POST_ID, -1)
    }

    override val viewModel by viewModel<ReportViewModel>()
    override fun getViewBinding(): ActivityReportBinding =
        ActivityReportBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.reportStateLiveData.observe(this) {
        when (it) {
            is ReportState.Uninitialized -> {}

            is ReportState.Loading -> {
                binding.clLoading.isVisible = true
                window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            is ReportState.Success -> {
                binding.clLoading.isGone = true
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                startActivity(ReportFinishActivity.newIntent(applicationContext))
                finish()
            }

            is ReportState.Error -> {
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

        ibPrev.setOnClickListener {
            finish()
        }

        tvFinish.setOnClickListener {
            when(rgReportReasons.checkedRadioButtonId) {
                R.id.rb_Spam -> {
                    viewModel.sendReport("SPAM", postId)
                }
                R.id.rb_HateSpeech -> {
                    viewModel.sendReport("HATE_SPEECH", postId)
                }

                R.id.rb_FalseInformation -> {
                    viewModel.sendReport("FALSE_INFORMATION", postId)
                }

                R.id.rb_SwearWord -> {
                    viewModel.sendReport("SWEAR_WORD", postId)
                }

                R.id.rb_Duplicate -> {
                    viewModel.sendReport("DUPLICATE", postId)
                }

                R.id.rb_Obscene -> {
                    viewModel.sendReport("OBSCENE", postId)
                }

                else -> {
                    Toast.makeText(applicationContext, "신고할 카테고리를 선택해주세요", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }
}