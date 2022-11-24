package com.hand.comeeatme.view.main.user.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.databinding.ActivityMycommentBinding

class MyCommentActivity: AppCompatActivity() {
    private var _binding: ActivityMycommentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMycommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initView() {

    }

    private fun initListener() {
        binding.toolbarMyComment.setNavigationOnClickListener {
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}