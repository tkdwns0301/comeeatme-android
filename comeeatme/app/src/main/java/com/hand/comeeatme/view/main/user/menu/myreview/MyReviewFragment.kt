package com.hand.comeeatme.view.main.user.menu.myreview

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.FragmentMyreviewBinding
import com.hand.comeeatme.util.widget.adapter.user.MyReviewAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyReviewFragment: BaseFragment<MyReviewViewModel, FragmentMyreviewBinding>() {
    companion object {
        const val TAG = "MyReviewFragment"
        fun newInstance() = MyReviewFragment()
    }

    override val viewModel by viewModel<MyReviewViewModel>()
    override fun getViewBinding(): FragmentMyreviewBinding = FragmentMyreviewBinding.inflate(layoutInflater)

    private lateinit var adapter : MyReviewAdapter

    override fun observeData()  {
        viewModel.myReviewStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is MyReviewState.Uninitialized -> {
                    viewModel.getUserPost()
                }

                is MyReviewState.Loading -> {

                }

                is MyReviewState.Success -> {
                    setAdapter(it.response.data.content)
                }

                is MyReviewState.Error -> {

                }
            }
        }

    }

    override fun initView() = with(binding){
        rvMyReview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        toolbarMyReview.setNavigationOnClickListener {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<Content>) {
        adapter = MyReviewAdapter(contents, requireContext())
        binding.rvMyReview.adapter = adapter
        adapter.notifyDataSetChanged()
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


}