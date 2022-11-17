package com.hand.comeeatme.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {
    abstract val viewModel: VM

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    abstract fun getViewBinding(): VB

    private lateinit var fetchJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        initState()
    }

    open fun initState() {
        initView()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initView() = Unit

    abstract fun observeData()

    override fun onDestroy() {
        if(fetchJob.isActive) {
            fetchJob.cancel()
        }

        _binding = null
        super.onDestroy()
    }
}