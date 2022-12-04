package com.hand.comeeatme.view.main.bookmark

import com.google.android.material.tabs.TabLayoutMediator
import com.hand.comeeatme.databinding.FragmentBookmarkBinding
import com.hand.comeeatme.util.widget.adapter.BookmarkAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.main.bookmark.favorite.FavoriteRestaurantFragment
import com.hand.comeeatme.view.main.bookmark.post.BookmarkPostFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarkFragment : BaseFragment<BookmarkViewModel, FragmentBookmarkBinding>() {
    companion object {
        fun newInstance() = BookmarkFragment()
        const val TAG = "BookmarkFragment"
    }

    override val viewModel by viewModel<BookmarkViewModel>()
    override fun getViewBinding() : FragmentBookmarkBinding = FragmentBookmarkBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.bookmarkStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is BookmarkState.Uninitialized -> {

                }

                is BookmarkState.Loading -> {

                }

                is BookmarkState.Success -> {

                }

                is BookmarkState.Error -> {

                }
            }
        }
    }

    override fun initView() = with(binding){
        tlContent.addTab(tlContent.newTab().setText("게시글"))
        tlContent.addTab(tlContent.newTab().setText("즐겨찾기"))

        val adapter = BookmarkAdapter(requireActivity())
        adapter.addFragment(BookmarkPostFragment())
        adapter.addFragment(FavoriteRestaurantFragment())

        binding.vpBookmark.adapter = adapter

        TabLayoutMediator(tlContent, binding.vpBookmark) { tab, position ->
            when(position) {
                0 -> tab.text = "게시글"
                1 -> tab.text = "즐겨찾기"
            }
        }.attach()
    }

}