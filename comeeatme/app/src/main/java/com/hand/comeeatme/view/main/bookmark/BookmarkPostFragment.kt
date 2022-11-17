package com.hand.comeeatme.view.main.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.R
import com.hand.comeeatme.util.widget.adapter.BookmarkPostAdapter
import com.hand.comeeatme.databinding.LayoutBookmarkPostBinding

class BookmarkPostFragment: Fragment(R.layout.layout_bookmark_post) {
    private var _binding : LayoutBookmarkPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : BookmarkPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBookmarkPostBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    private fun initView() {
        val items = ArrayList<Int>()

        items.add(0)
        items.add(0)
        items.add(0)
        items.add(0)
        items.add(0)
        items.add(0)



        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvPostList.layoutManager = layoutManager

        if(items != null) {
            adapter = BookmarkPostAdapter(requireContext(), items)
            binding.rvPostList.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}