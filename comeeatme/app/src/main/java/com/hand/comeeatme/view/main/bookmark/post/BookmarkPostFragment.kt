package com.hand.comeeatme.view.main.bookmark.post

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
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
                    viewModel.getAllBookmarked(0, 10)
                }
                is BookmarkPostState.Loading -> {
                    binding.clLoading.isVisible = true
                }

                is BookmarkPostState.Success -> {
                    binding.clLoading.isGone = true
                    setAdapter(it.response.data!!.content)
                }

                is BookmarkPostState.Error -> {
                    binding.clLoading.isGone = true
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
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

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        viewModel.getAllBookmarked(0, 10)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<BookmarkPostContent>) {
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
        adapter.notifyDataSetChanged()
    }
}