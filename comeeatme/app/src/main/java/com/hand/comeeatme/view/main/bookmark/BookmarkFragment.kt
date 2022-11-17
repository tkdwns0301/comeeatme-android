package com.hand.comeeatme.view.main.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.hand.comeeatme.R
import com.hand.comeeatme.util.widget.adapter.BookmarkAdapter
import com.hand.comeeatme.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    private fun initView() {
        val tabLayout = binding.tlContent
        tabLayout.addTab(tabLayout.newTab().setText("게시글"))
        tabLayout.addTab(tabLayout.newTab().setText("즐겨찾기"))

        val adapter = BookmarkAdapter(requireActivity())
        adapter.addFragment(BookmarkPostFragment())
        adapter.addFragment(BookmarkStarFragment())

        binding.vpBookmark.adapter = adapter

        TabLayoutMediator(tabLayout, binding.vpBookmark) { tab, position ->
            when(position) {
                0 -> tab.text = "게시글"
                1 -> tab.text = "즐겨찾기"
            }
        }.attach()
    }

    private fun initListener() {

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}