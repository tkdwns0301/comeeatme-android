package com.hand.comeeatme.view.main.user.setting.unlink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.databinding.ActivityUnlink1Binding
import com.hand.comeeatme.view.main.user.setting.MailActivity

class Unlink1Activity: AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, Unlink1Activity::class.java)
    }

    private var _binding: ActivityUnlink1Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUnlink1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        clMail.setOnClickListener {
            startActivity(MailActivity.newIntent(applicationContext))
            finish()
        }

        clNext.setOnClickListener {
            startActivity(Unlink2Activity.newIntent(applicationContext))
        }

        toolbarUnlink.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}