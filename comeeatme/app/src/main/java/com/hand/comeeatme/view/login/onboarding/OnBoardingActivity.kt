package com.hand.comeeatme.view.login.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hand.comeeatme.databinding.ActivityOnboardingBinding
import com.hand.comeeatme.util.widget.adapter.home.OnBoardingAdapter
import com.hand.comeeatme.view.main.MainActivity

class OnBoardingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, OnBoardingActivity::class.java)
    }


    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private var fragmentList = arrayListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentList.add(OnBoarding2Fragment())
        fragmentList.add(OnBoarding3Fragment())
        fragmentList.add(OnBoarding4Fragment())

        onBoardingAdapter = OnBoardingAdapter(this)

        binding.vpImages.adapter = onBoardingAdapter
        binding.dotsIndicator.setViewPager2(binding.vpImages)

        binding.tvFinish.setOnClickListener {
            startActivity(MainActivity.newIntent(applicationContext))
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}