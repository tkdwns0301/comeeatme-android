package com.hand.comeeatme.util.widget.adapter.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hand.comeeatme.view.login.onboarding.OnBoarding2Fragment
import com.hand.comeeatme.view.login.onboarding.OnBoarding3Fragment
import com.hand.comeeatme.view.login.onboarding.OnBoarding4Fragment

class OnBoardingAdapter(
    fa: FragmentActivity
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> OnBoarding2Fragment()
            1 -> OnBoarding3Fragment()
            else -> OnBoarding4Fragment()
        }
    }


}

