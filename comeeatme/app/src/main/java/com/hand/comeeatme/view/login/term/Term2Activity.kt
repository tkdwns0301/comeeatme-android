package com.hand.comeeatme.view.login.term

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.databinding.ActivityTerm1Binding

class Term2Activity: AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, Term1Activity::class.java)
    }

    private var _binding: ActivityTerm1Binding? = null
    private val binding get() =_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTerm1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding){
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true

        webView.loadUrl("https://amused-sing-8d6.notion.site/c4c2d1f04b79439ab4dc7a18c93e741c")
    }
}