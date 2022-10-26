package com.hand.comeeatme.view.main.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.R
import com.hand.comeeatme.adapter.BookmarkStarAdapter
import com.hand.comeeatme.databinding.LayoutBookmarkStarBinding

class BookmarkStarFragment: Fragment(R.layout.layout_bookmark_star) {
    private var _binding : LayoutBookmarkStarBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BookmarkStarAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBookmarkStarBinding.inflate(inflater, container, false)

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

        binding.rvStarList.layoutManager = layoutManager

        if(items != null) {
            adapter = BookmarkStarAdapter(requireContext(), items)
            binding.rvStarList.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}