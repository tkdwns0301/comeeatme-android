package com.hand.comeeatme.view.main.user.setting.notice

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.notice.NoticesContent
import com.hand.comeeatme.databinding.ActivityNoticeBinding
import com.hand.comeeatme.util.widget.adapter.setting.NoticeAdapter
import com.hand.comeeatme.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoticeActivity : BaseActivity<NoticeViewModel, ActivityNoticeBinding>() {

    companion object {
        fun newIntent(context: Context) = Intent(context, NoticeActivity::class.java)
    }

    private lateinit var adapter: NoticeAdapter

    override val viewModel by viewModel<NoticeViewModel>()
    override fun getViewBinding(): ActivityNoticeBinding = ActivityNoticeBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.noticeStateLiveData.observe(this){
        when(it) {
            is NoticeState.Loading -> {
                binding.clLoading.isVisible = true
                window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            is NoticeState.Success -> {
                binding.clLoading.isGone = true
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                setAdapter(it.response)
            }

            is NoticeState.Error -> {
                binding.clLoading.isGone = true
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initView() = with(binding){
        Glide.with(applicationContext)
            .load(R.drawable.loading)
            .into(ivLoading)

        toolbarNotice.setNavigationOnClickListener {
            finish()
        }

        rvNotice.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        rvNotice.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!viewModel.getIsLast()) {
                    if(!rvNotice.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getNotices(false)
                    }
                }
            }
        })

        srlNotice.setOnRefreshListener {
            viewModel.getNotices(true)
            srlNotice.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: ArrayList<NoticesContent>) {
        if(contents.isNotEmpty()) {
            val recyclerViewState = binding.rvNotice.layoutManager?.onSaveInstanceState()
            adapter = NoticeAdapter(
                applicationContext,
                contents
            )

            binding.rvNotice.adapter= adapter
            binding.rvNotice.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }
    }
}