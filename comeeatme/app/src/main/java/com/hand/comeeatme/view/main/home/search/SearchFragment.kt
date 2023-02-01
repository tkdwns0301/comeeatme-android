package com.hand.comeeatme.view.main.home.search

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.member.MemberSearchContent
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent
import com.hand.comeeatme.databinding.FragmentSearchBinding
import com.hand.comeeatme.util.widget.adapter.home.SearchRestaurantAdapter
import com.hand.comeeatme.util.widget.adapter.home.SearchUserAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {
    companion object {
        const val TAG = "SearchFragment"

        fun newInstance() = SearchFragment()
    }

    override val viewModel by viewModel<SearchViewModel>()
    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    private lateinit var userAdapter: SearchUserAdapter
    private lateinit var restaurantAdapter: SearchRestaurantAdapter

    override fun observeData() {
        viewModel.searchStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is SearchState.Uninitialized -> {
                }

                is SearchState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is SearchState.SearchUserSuccess -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setUserAdapter(it.response!!.data.content)
                }

                is SearchState.SearchRestaurantSuccess -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setRestaurantAdapter(it.response)
                }

                is SearchState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        rvSearchList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        ibPrev.setOnClickListener {
            finish()
        }

        ibSearch.setOnClickListener {
            searchAnything("${etSearch.text}")

        }


        etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                    searchAnything("${etSearch.text}")
                    return true
                }
                return false
            }

        })

        rvSearchList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!viewModel.getIsLast()) {
                    if(!rvSearchList.canScrollVertically(1) && viewModel.getType()) {
                        viewModel.setIsLast(true)
                        viewModel.getSearchRestaurants(false)
                    }
                }
            }
        })

    }

    private fun searchAnything(query: String) = with(binding) {
        if (query.isNotEmpty()) {
            if (query[0] == '@') {
                viewModel.setType(false)
                viewModel.setQuery(query.substring(1, query.length))
                viewModel.getSearchNicknames()
            } else {
                viewModel.setType(true)
                viewModel.setQuery(query)
                viewModel.getSearchRestaurants(true)
            }
        }

        binding.etSearch.clearFocus()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUserAdapter(contents: List<MemberSearchContent>) {
        userAdapter = SearchUserAdapter(
            contents,
            requireContext()
        )
        binding.rvSearchList.adapter = userAdapter
        userAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRestaurantAdapter(contents: List<SimpleRestaurantContent>) {
        if(contents.isNotEmpty()) {
            val recyclerViewState = binding.rvSearchList.layoutManager?.onSaveInstanceState()

            restaurantAdapter = SearchRestaurantAdapter(
                requireContext(),
                contents
            )

            binding.rvSearchList.adapter = restaurantAdapter
            binding.rvSearchList.layoutManager?.onRestoreInstanceState(recyclerViewState)
            restaurantAdapter.notifyDataSetChanged()
        }


    }

    private fun finish() {
        val manager: FragmentManager? = activity?.supportFragmentManager
        val ft: FragmentTransaction = manager!!.beginTransaction()

        val fragment = manager.findFragmentByTag(TAG)

        if (fragment != null) {
            ft.remove(fragment)
        }

        ft.commitAllowingStateLoss()
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