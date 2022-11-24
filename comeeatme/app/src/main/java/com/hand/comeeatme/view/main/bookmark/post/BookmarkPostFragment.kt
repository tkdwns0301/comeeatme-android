package com.hand.comeeatme.view.main.bookmark.post

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
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

                }
                is BookmarkPostState.Loading -> {

                }

                is BookmarkPostState.Success -> {
                    setAdapter(it.response.data.content)
                }

                is BookmarkPostState.Error -> {

                }
            }
        }
    }

    private lateinit var adapter: BookmarkPostAdapter

    override fun initView() = with(binding) {
        rvPostList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getAllBookmarked(0, 10, false)

        srlBookmarkPostList.setOnRefreshListener {
            refresh()
        }
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

    private fun refresh() = with(binding) {
        viewModel.getAllBookmarked(0, 10, false)
        srlBookmarkPostList.isRefreshing = false
    }
}