package com.hand.comeeatme.view.login.term

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityTermBinding
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.login.onboarding.OnBoardingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class TermActivity: BaseActivity<TermViewModel, ActivityTermBinding>() {
    companion object {

        fun newIntent(context: Context) =
            Intent(context, TermActivity::class.java)
    }

    override val viewModel by viewModel<TermViewModel>()
    override fun getViewBinding(): ActivityTermBinding = ActivityTermBinding.inflate(layoutInflater)

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun observeData() = viewModel.termStateLiveData.observe(this) {
        when(it) {
            is TermState.Uninitialized -> {
            }

            is TermState.Loading -> {
                binding.clLoading.isVisible = true
                window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            is TermState.Success -> {
                binding.clLoading.isGone = true
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                startActivity(
                    OnBoardingActivity.newIntent(applicationContext)
                )
                finish()
            }

            is TermState.TermAllReady -> {
                binding.clNext.background = applicationContext.getDrawable(R.drawable.background_next_checked)
                binding.tvNext.setTextColor(applicationContext.getColor(R.color.white))
            }

            is TermState.TermNotReady -> {
                binding.clNext.background = applicationContext.getDrawable(R.drawable.background_next_unchecked)
                binding.tvNext.setTextColor(applicationContext.getColor(R.color.basic))
            }

            is TermState.Error -> {
                binding.clLoading.isGone = true
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                Toast.makeText(applicationContext, "$it", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initView() = with(binding) {
        Glide.with(applicationContext)
            .load(R.drawable.loading)
            .into(ivLoading)

        toolbarTerm.setNavigationOnClickListener {
            finish()
        }

        tbAgree.setOnClickListener {
            tbTerm.isChecked = tbAgree.isChecked
            tbTerm2.isChecked = tbAgree.isChecked
        }

        tbTerm.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTerm1(isChecked)
        }

        tbTerm2.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTerm2(isChecked)
        }

        clNext.setOnClickListener {
            if(tvNext.currentTextColor == applicationContext.getColor(R.color.white)) {
                viewModel.setTermsAgree()
            }
        }

        clTerm.setOnClickListener {
            startActivity(Term1Activity.newIntent(applicationContext))
        }

        clTerm2.setOnClickListener {
            startActivity(Term2Activity.newIntent(applicationContext))
        }


    }
}