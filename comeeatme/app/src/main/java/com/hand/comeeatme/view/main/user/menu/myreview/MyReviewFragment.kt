package com.hand.comeeatme.view.main.user.menu.myreview

import android.annotation.SuppressLint
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.FragmentMyreviewBinding
import com.hand.comeeatme.util.widget.adapter.user.MyReviewAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyReviewFragment: BaseFragment<MyReviewViewModel, FragmentMyreviewBinding>() {
    companion object {
        const val TAG = "MyReviewFragment"
        fun newInstance() = MyReviewFragment()
    }

    override val viewModel by viewModel<MyReviewViewModel>()
    override fun getViewBinding(): FragmentMyreviewBinding = FragmentMyreviewBinding.inflate(layoutInflater)

    private lateinit var adapter : MyReviewAdapter

    override fun observeData()  {
        viewModel.myReviewStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is MyReviewState.Uninitialized -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    viewModel.getUserPost(true)
                }

                is MyReviewState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is MyReviewState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setAdapter(it.response)
                }

                is MyReviewState.Error -> {
                    binding.clLoading.isGone=true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun initView() = with(binding){
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        rvMyReview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        toolbarMyReview.setNavigationOnClickListener {
            finish()
        }

        srlMyReview.setOnRefreshListener {
            viewModel.getUserPost(true)
            srlMyReview.isRefreshing = false
        }

        rvMyReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!viewModel.getIsLast()) {
                    if(!rvMyReview.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getUserPost(false)
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<Content>) {
        if(contents.isNotEmpty()) {
            val recyclerViewState = binding.rvMyReview.layoutManager?.onSaveInstanceState()

            adapter = MyReviewAdapter(contents, requireContext())
            binding.rvMyReview.adapter = adapter
            binding.rvMyReview.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }


    }

    private fun finish() {
        val manager: FragmentManager? = activity?.supportFragmentManager
        val ft: FragmentTransaction = manager!!.beginTransaction()

        val fragment = manager.findFragmentByTag(TAG)

        if (fragment != null) {
            ft.remove(fragment)
        }

        ft.commitAllowingStateLoss()
    }


}