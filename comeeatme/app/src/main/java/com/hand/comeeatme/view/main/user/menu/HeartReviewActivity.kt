package com.hand.comeeatme.view.main.user.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.databinding.ActivityHeartreviewBinding

class HeartReviewActivity: AppCompatActivity() {
    private var _binding: ActivityHeartreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHeartreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initView() {

    }

    private fun initListener() {
        binding.toolbarHeartReview.setNavigationOnClickListener {
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}