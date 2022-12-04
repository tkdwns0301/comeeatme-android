package com.hand.comeeatme.view.main.bookmark.favorite

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.data.response.favorite.FavoritePostContent
import com.hand.comeeatme.databinding.LayoutBookmarkFavoriteBinding
import com.hand.comeeatme.util.widget.adapter.bookmark.FavoriteRestaurantAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteRestaurantFragment: BaseFragment<FavoriteRestaurantViewModel, LayoutBookmarkFavoriteBinding>() {
    companion object {
        fun newInstance() = FavoriteRestaurantFragment()
        const val TAG = "FavoriteRestaurantFragment"
    }

    override val viewModel by viewModel<FavoriteRestaurantViewModel>()
    override fun getViewBinding(): LayoutBookmarkFavoriteBinding = LayoutBookmarkFavoriteBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.favoritePostStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteRestaurantState.Uninitialized -> {

                }

                is FavoriteRestaurantState.Loading -> {

                }

                is FavoriteRestaurantState.Success -> {
                    setAdapter(it.response.data.content)
                }

                is FavoriteRestaurantState.Error -> {

                }
            }
        }
    }

    private lateinit var adapter: FavoriteRestaurantAdapter

    override fun initView() {
        binding.rvStarList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getAllFavorite(0, 10, false)


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<FavoritePostContent>) {
        adapter = FavoriteRestaurantAdapter(
            requireContext(),
            contents,
            favoriteRestaurant = {
                viewModel.favoriteRestaurant(it)
            },
            unFavoriteRestaurant = {
                viewModel.unFavoriteRestaurant(it)
            }
        )
        binding.rvStarList.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}