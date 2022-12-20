package com.hand.comeeatme.view.main.home

import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.FragmentHomeBinding
import com.hand.comeeatme.util.widget.adapter.home.CommunityAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.login.onboarding.OnBoardingActivity
import com.hand.comeeatme.view.main.home.hashtag.HashTagActivity
import com.hand.comeeatme.view.main.home.search.SearchFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }

    override val viewModel by viewModel<HomeViewModel>()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var adapter: CommunityAdapter

    override fun observeData() {
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeState.Uninitialized -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is HomeState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is HomeState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setAdapter(it.posts)
                }

                is HomeState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        rvHomeList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        srlHomeList.setOnRefreshListener {
            refresh()
        }

        ibHashTag.setOnClickListener {
            startActivityForResult(HashTagActivity.newIntent(requireContext(),
                viewModel.getCheckedChipList()), 100)
        }

        ibSearch.setOnClickListener {
            val manager: FragmentManager =
                (requireContext() as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, SearchFragment.newInstance(), SearchFragment.TAG)
            ft.addToBackStack(SearchFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        ibNotification.setOnClickListener {
            // TODO Notification
            val intent = Intent(requireContext(), OnBoardingActivity::class.java)
            startActivity(intent)

        }

        rvHomeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!viewModel.getIsLast()) {
                    if (!binding.rvHomeList.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.loadPost(false)
                    }
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 100) {
            viewModel.setCheckedChipList(data!!.getStringArrayListExtra(HashTagActivity.CHECKED_HASHTAG) as ArrayList<String>)

            if (viewModel.getCheckedChipList().size == 0) {
                binding.clHashTagNum.visibility = View.INVISIBLE
                viewModel.loadPost(true)
            } else {
                binding.clHashTagNum.visibility = View.VISIBLE
                binding.tvHashTag.text = "${viewModel.getCheckedChipList().size}"
                viewModel.loadPost(true)
            }

        }

    }

    private fun setAdapter(contents: ArrayList<Content>) {
        if (contents.isNotEmpty()) {
            val recyclerViewState = binding.rvHomeList.layoutManager?.onSaveInstanceState()

            adapter = CommunityAdapter(
                contents,
                requireContext(),
                likePost = {
                    viewModel.likePost(it)
                }, unLikePost = {
                    viewModel.unLikePost(it)
                }, bookmarkPost = {
                    viewModel.bookmarkPost(it)
                }, unBookmarkPost = {
                    viewModel.unBookmarkPost(it)
                }
            )
            binding.rvHomeList.adapter = adapter
            binding.rvHomeList.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }


    }


    private fun refresh() = with(binding) {
        viewModel.loadPost(true)
        srlHomeList.isRefreshing = false
    }

}