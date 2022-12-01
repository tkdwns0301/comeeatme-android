package com.hand.comeeatme.view.main.home.search

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.data.response.home.NicknameContent
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent
import com.hand.comeeatme.databinding.ActivitySearchBinding
import com.hand.comeeatme.util.widget.adapter.home.SearchRestaurantAdapter
import com.hand.comeeatme.util.widget.adapter.home.SearchUserAdapter
import com.hand.comeeatme.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<SearchViewModel, ActivitySearchBinding>() {

    override val viewModel by viewModel<SearchViewModel>()
    override fun getViewBinding(): ActivitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)

    private lateinit var userAdapter: SearchUserAdapter
    private lateinit var restaurantAdapter: SearchRestaurantAdapter

    override fun observeData() = viewModel.searchStateLiveData.observe(this) {
        when(it) {
            is SearchState.Uninitialized -> {
                binding.clLoading.isGone = true
            }

            is SearchState.Loading -> {
                binding.clLoading.isVisible = true
            }

            is SearchState.SearchUserSuccess -> {
                binding.clLoading.isGone = true
                setUserAdapter(it.response!!.data.content)
            }

            is SearchState.SearchRestaurantSuccess -> {
                binding.clLoading.isGone = true
                setRestaurantAdapter(it.response!!.data.content)
            }

            is SearchState.Error -> {
                binding.clLoading.isGone = true
            }
        }

    }


    override fun initView() = with(binding){
        rvSearchList.layoutManager =LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        ibPrev.setOnClickListener {
            finish()
        }

        ibSearch.setOnClickListener {
            searchAnything("${etSearch.text}")

        }


        etSearch.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == EditorInfo.IME_ACTION_SEARCH) {
                    searchAnything("${etSearch.text}")
                    return true
                }
                return false
            }

        })

    }

    private fun searchAnything(query: String) = with(binding){
        if(query.isNotEmpty()) {
            if(query[0] == '@') {
                viewModel.getSearchNicknames(query.substring(1, query.length))
            } else {
                viewModel.getSearchRestaurants(null, null, null, query)
            }
        }

        binding.etSearch.clearFocus()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUserAdapter(contents: List<NicknameContent>) {
        userAdapter = SearchUserAdapter(
            contents,
            applicationContext
        )
        binding.rvSearchList.adapter = userAdapter
        userAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRestaurantAdapter(contents: List<SimpleRestaurantContent>) {
        restaurantAdapter = SearchRestaurantAdapter(
            applicationContext,
            contents
        )

        binding.rvSearchList.adapter = restaurantAdapter
        restaurantAdapter.notifyDataSetChanged()
    }

//    private fun setPreferences(context: Context) {
//        val pref = PreferenceManager.getDefaultSharedPreferences(context)
//        val editor = pref.edit()
//        val jsonArray = JSONArray()
//
//        for (i in 0 until recentSearchList.size) {
//            jsonArray.put(recentSearchList[i])
//        }
//
//        if (jsonArray.length() != 0) {
//            editor.putString("RecentSearch", jsonArray.toString())
//        } else {
//            editor.putString("RecentSearch", null)
//        }
//
//        editor.apply()
//    }
//
//    private fun getPreferences(context: Context) {
//        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
//        val jsonString = prefs.getString("RecentSearch", null)
//
//        if (jsonString != null) {
//            try {
//                val jsonArray = JSONArray(jsonString)
//
//                for (i in 0 until jsonArray.length()) {
//                    val searchQuery = jsonArray[i].toString()
//                    recentSearchList.add(searchQuery)
//                }
//            } catch (e: JSONException) {
//                e("JSON ERROR", "${e.printStackTrace()}")
//            }
//        }
//
//        e("getPreferences", "$recentSearchList")
//    }
//
//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//        //currentFocus!!.clearFocus()
//
//
//        return super.dispatchTouchEvent(ev)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        setPreferences(this)
//    }
}