package com.hand.comeeatme.view.main.bookmark.favorite

import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.data.response.favorite.FavoritePostContent
import com.hand.comeeatme.databinding.LayoutBookmarkFavoriteBinding
import com.hand.comeeatme.util.widget.adapter.bookmark.FavoritePostAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritePostFragment: BaseFragment<FavoritePostViewModel, LayoutBookmarkFavoriteBinding>() {
    companion object {
        fun newInstance() = FavoritePostFragment()
        const val TAG = "FavoritePostFragment"
    }

    override val viewModel by viewModel<FavoritePostViewModel>()
    override fun getViewBinding(): LayoutBookmarkFavoriteBinding = LayoutBookmarkFavoriteBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.favoritePostStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is FavoritePostState.Uninitialized -> {

                }

                is FavoritePostState.Loading -> {

                }

                is FavoritePostState.Success -> {
                    setAdapter(it.response.data.content)
                }

                is FavoritePostState.Error -> {

                }
            }
        }
    }

    private lateinit var adapter: FavoritePostAdapter

    override fun initView() {
        binding.rvStarList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getAllFavorite(0, 10, false)


    }

    private fun setAdapter(contents: List<FavoritePostContent>) {
        adapter = FavoritePostAdapter(requireContext(), contents)
        binding.rvStarList.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}