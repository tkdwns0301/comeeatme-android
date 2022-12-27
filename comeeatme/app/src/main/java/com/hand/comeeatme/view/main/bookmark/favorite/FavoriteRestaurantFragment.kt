package com.hand.comeeatme.view.main.bookmark.favorite

import android.annotation.SuppressLint
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.favorite.FavoritePostContent
import com.hand.comeeatme.databinding.LayoutBookmarkFavoriteBinding
import com.hand.comeeatme.util.widget.adapter.bookmark.FavoriteRestaurantAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteRestaurantFragment: BaseFragment<FavoriteRestaurantViewModel, LayoutBookmarkFavoriteBinding>(), LifecycleObserver {
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
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    viewModel.getAllFavorite(true)
                }

                is FavoriteRestaurantState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is FavoriteRestaurantState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setAdapter(it.response)
                }

                is FavoriteRestaurantState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        viewModel.getAllFavorite(true)
    }


    private lateinit var adapter: FavoriteRestaurantAdapter

    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        rvFavoriteList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rvFavoriteList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!viewModel.getIsLast()) {
                    if(!rvFavoriteList.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getAllFavorite(false)
                    }
                }
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<FavoritePostContent>) {
        if(contents.isNotEmpty()) {
            val recyclerViewState = binding.rvFavoriteList.layoutManager?.onSaveInstanceState()

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
            binding.rvFavoriteList.adapter = adapter
            binding.rvFavoriteList.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }
    }
}