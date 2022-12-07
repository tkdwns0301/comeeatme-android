package com.hand.comeeatme.view.main.home.post.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.databinding.ActivityReportFinishBinding

class ReportFinishActivity: AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, ReportFinishActivity::class.java)
    }

    private var _binding: ActivityReportFinishBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReportFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding){
        clPrev.setOnClickListener {
            finish()
        }
    }
}