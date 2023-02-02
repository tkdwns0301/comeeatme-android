package com.hand.comeeatme.view.login.term

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.databinding.ActivityTerm1Binding

class Term1Activity: AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, Term2Activity::class.java)
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

        webView.loadUrl("https://amused-sing-8d6.notion.site/e28802cf90164d42aefe770a55901758")

    }
}