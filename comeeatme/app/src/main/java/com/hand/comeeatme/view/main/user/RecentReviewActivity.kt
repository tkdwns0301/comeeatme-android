package com.hand.comeeatme.view.main.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.databinding.ActivityRecentreviewBinding

class RecentReviewActivity: AppCompatActivity() {
    private var _binding: ActivityRecentreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecentreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initView() {

    }

    private fun initListener() {
        binding.toolbarRecentReview.setNavigationOnClickListener {
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}