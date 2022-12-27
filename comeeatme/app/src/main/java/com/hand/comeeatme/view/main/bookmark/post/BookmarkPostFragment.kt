package com.hand.comeeatme.view.main.bookmark.post

import android.annotation.SuppressLint
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.bookmark.BookmarkPostContent
import com.hand.comeeatme.databinding.LayoutBookmarkPostBinding
import com.hand.comeeatme.util.widget.adapter.bookmark.BookmarkPostAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarkPostFragment : BaseFragment<BookmarkPostViewModel, LayoutBookmarkPostBinding>() {
    companion object {
        fun newInstance() = BookmarkPostFragment()
        const val TAG = "BookmarkPostFragment"
    }

    override val viewModel by viewModel<BookmarkPostViewModel>()
    override fun getViewBinding(): LayoutBookmarkPostBinding =
        LayoutBookmarkPostBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.bookmarkPostStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is BookmarkPostState.Uninitialized -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    viewModel.getAllBookmarked(true)
                }
                is BookmarkPostState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is BookmarkPostState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setAdapter(it.response)
                }

                is BookmarkPostState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private lateinit var adapter: BookmarkPostAdapter

    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        rvPostList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rvPostList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!viewModel.getIsLast()) {
                    if(!rvPostList.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getAllBookmarked(false)
                    }
                }
            }
        })

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        viewModel.getAllBookmarked(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<BookmarkPostContent>) {
        if(contents.isNotEmpty()) {
            val recyclerViewState = binding.rvPostList.layoutManager?.onSaveInstanceState()
            adapter = BookmarkPostAdapter(
                requireContext(), contents,
                bookmarkPost = {
                    viewModel.bookmarkPost(it)
                },
                unBookmarkPost = {
                    viewModel.unBookmarkPost(it)
                }
            )
            binding.rvPostList.adapter = adapter
            binding.rvPostList.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }


    }
}