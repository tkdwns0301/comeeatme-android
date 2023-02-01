package com.hand.comeeatme.view.main.user.menu.mycomment

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
import com.hand.comeeatme.data.response.comment.MemberCommentContent
import com.hand.comeeatme.databinding.FragmentMycommentBinding
import com.hand.comeeatme.util.widget.adapter.user.MyCommentsAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyCommentFragment: BaseFragment<MyCommentViewModel, FragmentMycommentBinding>() {
    companion object {
        const val TAG = "MyCommentFragment"

        fun newInstance() = MyCommentFragment()
    }

    override val viewModel by viewModel<MyCommentViewModel>()
    override fun getViewBinding(): FragmentMycommentBinding = FragmentMycommentBinding.inflate(layoutInflater)

    private lateinit var adapter: MyCommentsAdapter

    override fun observeData() {
        viewModel.myCommentStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is MyCommentState.Uninitialized ->{
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is MyCommentState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is MyCommentState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setAdapter(it.response)
                }

                is MyCommentState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        rvMyComments.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        toolbarMyComment.setNavigationOnClickListener {
            finish()
        }

        rvMyComments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!viewModel.getIsLast()) {
                    if(!rvMyComments.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getMemberComments(false)
                    }
                }
            }
        })

        srlMyComments.setOnRefreshListener {
            viewModel.getMemberComments(true)
            srlMyComments.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: ArrayList<MemberCommentContent>) {
        if(contents.isNotEmpty()) {
            val recyclerViewState = binding.rvMyComments.layoutManager?.onSaveInstanceState()

            adapter = MyCommentsAdapter(
                requireContext(),
                contents,
            )

            binding.rvMyComments.adapter = adapter
            binding.rvMyComments.layoutManager?.onRestoreInstanceState(recyclerViewState)
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