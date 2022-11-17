package com.hand.comeeatme.view.main.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.databinding.ActivityMapDetailBinding
import com.hand.comeeatme.util.widget.adapter.MapDetailAdapter

class MapDetailActivity : AppCompatActivity() {
    private var _binding : ActivityMapDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : MapDetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initView() {

        val items = ArrayList<Int>()

        items.add(0)
        items.add(0)
        items.add(0)
        items.add(0)
        items.add(0)
        items.add(0)



        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvIncludingContent.layoutManager = layoutManager

        if(items != null) {
            adapter = MapDetailAdapter(applicationContext, items)
            binding.rvIncludingContent.adapter = adapter
            adapter.notifyDataSetChanged()
        }


    }

    private fun initListener() {
        binding.toolbarMapDetail.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}