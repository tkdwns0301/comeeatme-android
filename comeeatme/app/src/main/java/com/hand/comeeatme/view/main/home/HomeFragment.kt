package com.hand.comeeatme.view.main.home

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.FragmentHomeBinding
import com.hand.comeeatme.util.widget.adapter.home.CommunityAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.main.home.hashtag.HashTagActivity
import com.hand.comeeatme.view.main.home.search.SearchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }

    override val viewModel by viewModel<HomeViewModel>()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var adapter: CommunityAdapter
    private var checkedChipList = ArrayList<String>()

    override fun observeData() {
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeState.Uninitialized -> {

                }

                is HomeState.Loading -> {
                    binding.clLoading.isVisible = true
                }

                is HomeState.Success -> {
                    binding.clLoading.isGone = true
                    setAdapter(it.posts.data.content)
                }

                is HomeState.Error -> {
                    binding.clLoading.isGone = true
                }
            }
        }
    }

    override fun initView() = with(binding) {
        rvHomeList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        rvHomeList.addOnScrollListener(VisiblePositionChangeListener(rvHomeList.layoutManager as LinearLayoutManager,
//        object: VisiblePositionChangeListener.OnChangeListener{
//            override fun onFirstVisiblePositionChanged(position: Int) {
//                visited[position] = true
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onLastVisiblePositionChanged(position: Int) {
//                visited[position] = true
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onFirstInvisiblePositionChanged(position: Int) {
//                visited[position] = true
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onLastInvisiblePositionChanged(position: Int) {
//                visited[position] = true
//                adapter.notifyDataSetChanged()
//            }
//
//        }))


        viewModel.loadPost(0, 10, false, null)

        srlHomeList.setOnRefreshListener {
            refresh()
        }

        ibHashTag.setOnClickListener {
            val intent = Intent(requireContext(), HashTagActivity::class.java)
            intent.putExtra("checkedChip", checkedChipList)
            startActivityForResult(intent, 100)
        }

        ibSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        srlHomeList.setOnRefreshListener {
            refresh()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 100) {
            checkedChipList = data!!.getStringArrayListExtra("checkedChip") as ArrayList<String>
            Log.e("checkedChipList", "$checkedChipList")

            if (checkedChipList.size == 0) {
                binding.clHashTagNum.visibility = View.INVISIBLE
            } else {
                binding.clHashTagNum.visibility = View.VISIBLE
                binding.tvHashTag.text = "${checkedChipList.size}"
            }

        }

    }

    private fun setAdapter(contents: List<Content>) {
        adapter = CommunityAdapter(contents, requireContext(), likePost = {
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
        adapter.notifyDataSetChanged()
    }


    private fun refresh() = with(binding) {
        viewModel.loadPost(0, 10, false, null)
        srlHomeList.isRefreshing = false
    }

}