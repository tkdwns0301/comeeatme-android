package com.hand.comeeatme.view.main.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //TODO EditText focus 죽이기

        initView()
        initListener()
    }

    private fun initView() {
        // 최근 검색어 보여주기
    }

    private fun initListener() {
        binding.toolbarSearch.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_Search -> {
                    //TODO 검색해서 아래 리스트에 뿌려주기
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)

                }
            }
        }

        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}