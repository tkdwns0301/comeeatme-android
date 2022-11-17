package com.hand.comeeatme.view.main.home.search

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivitySearchBinding
import com.hand.comeeatme.util.widget.adapter.RecentSearchAdapter
import com.hand.comeeatme.view.base.BaseActivity
import org.json.JSONArray
import org.json.JSONException
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<SearchViewModel, ActivitySearchBinding>() {

    override val viewModel by viewModel<SearchViewModel>()
    override fun getViewBinding(): ActivitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)

    private lateinit var adapter: RecentSearchAdapter

    private var recentSearchList = ArrayList<String>()

    override fun observeData() = viewModel.searchStateLiveData.observe(this) {
        when(it) {
            is SearchState.Uninitialized -> {
                binding.pbLoading.isGone = true
            }

            is SearchState.Loading -> {
                binding.pbLoading.isVisible = true
            }

            is SearchState.Success -> {
                binding.pbLoading.isGone = true
            }

            is SearchState.Error -> {
                binding.pbLoading.isGone = true
            }
        }

    }


    override fun initView() = with(binding){
        toolbarSearch.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_Search -> {
                    if (!recentSearchList.contains("${binding.etSearch.text}")) {
                        recentSearchList.add(0, "${binding.etSearch.text}")
                        adapter.notifyDataSetChanged()
                    } else {
                        recentSearchList.remove("${binding.etSearch.text}")
                        recentSearchList.add(0, "${binding.etSearch.text}")
                        adapter.notifyDataSetChanged()
                    }

                    binding.etSearch.text = null
                    binding.etSearch.clearFocus()
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

        etSearch.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == EditorInfo.IME_ACTION_SEARCH) {
                    if (!recentSearchList.contains("${binding.etSearch.text}")) {
                        recentSearchList.add(0, "${binding.etSearch.text}")
                        adapter.notifyDataSetChanged()
                    } else {
                        recentSearchList.remove("${binding.etSearch.text}")
                        recentSearchList.add(0, "${binding.etSearch.text}")
                        adapter.notifyDataSetChanged()
                    }

                    binding.etSearch.text = null
                    binding.etSearch.clearFocus()
                    return true
                }
                return false
            }

        })

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.rvRecentSearch.layoutManager = layoutManager

        setAdapter()
    }

    private fun setAdapter() {
        adapter = RecentSearchAdapter(
            recentSearchList,
            onClickDeleteIcon = {
                deleteTask(it)
            }
        )
        binding.rvRecentSearch.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun deleteTask(position: Int) {
        recentSearchList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    private fun setPreferences(context: Context) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()
        val jsonArray = JSONArray()

        for (i in 0 until recentSearchList.size) {
            jsonArray.put(recentSearchList[i])
        }

        if (jsonArray.length() != 0) {
            editor.putString("RecentSearch", jsonArray.toString())
        } else {
            editor.putString("RecentSearch", null)
        }

        editor.apply()
    }

    private fun getPreferences(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val jsonString = prefs.getString("RecentSearch", null)

        if (jsonString != null) {
            try {
                val jsonArray = JSONArray(jsonString)

                for (i in 0 until jsonArray.length()) {
                    val searchQuery = jsonArray[i].toString()
                    recentSearchList.add(searchQuery)
                }
            } catch (e: JSONException) {
                Log.e("JSON ERROR", "${e.printStackTrace()}")
            }
        }

        Log.e("getPreferences", "$recentSearchList")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        //currentFocus!!.clearFocus()


        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        setPreferences(this)
    }
}