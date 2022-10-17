package com.hand.comeeatme.view.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hand.comeeatme.R
import com.hand.comeeatme.adapter.CommunityAdapter
import com.hand.comeeatme.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommunityAdapter
    private var checkedChipList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()
        return binding.root
    }

    private fun initView() {
        swipe = binding.srlHomeList
        recyclerView = binding.rvHomeList
        binding.clHashTagNum.visibility = View.INVISIBLE

        initListener()
        initRecyclerView()
    }

    private fun initListener() {
        swipe.setOnRefreshListener {
            refresh()
        }

        binding.ibHashTag.setOnClickListener {
            Log.e("HashTag", "clicked")
            val intent = Intent(requireContext(), HashTagActivity::class.java)
            intent.putExtra("checkedChip", checkedChipList)
            startActivityForResult(intent, 100)
        }

        binding.ibSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
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


    private fun initRecyclerView() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        setAdapter()
    }

    private fun setAdapter() {
        if (getList() != null) {
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            adapter = CommunityAdapter(getList())
            recyclerView.adapter = adapter
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }
    }

    private fun refresh() {
        initRecyclerView()
        swipe.isRefreshing = false
    }

    private fun getList(): ArrayList<ArrayList<Int>> {
        val items = ArrayList<ArrayList<Int>>()

        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))
        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))
        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))
        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))
        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))

        return items
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}