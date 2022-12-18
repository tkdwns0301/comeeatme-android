package com.hand.comeeatme.view.login.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.LayoutOnboarding4Binding

class OnBoarding4Fragment : Fragment(R.layout.layout_onboarding4) {
    private var _binding : LayoutOnboarding4Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutOnboarding4Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}