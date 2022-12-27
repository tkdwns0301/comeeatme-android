package com.hand.comeeatme.view.login.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.LayoutOnboarding3Binding

class OnBoarding3Fragment : Fragment(R.layout.layout_onboarding3) {
    private var _binding : LayoutOnboarding3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutOnboarding3Binding.inflate(inflater, container, false)

        Glide.with(requireContext())
            .load(R.drawable.onboarding3)
            .into(binding.iv1)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}