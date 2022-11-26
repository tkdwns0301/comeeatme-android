package com.hand.comeeatme.view.main.user.menu.heartreview

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hand.comeeatme.databinding.FragmentHeartreviewBinding
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeartReviewFragment : BaseFragment<HeartReviewViewModel, FragmentHeartreviewBinding>() {
    companion object {
        const val TAG = "HeartReviewFragment"

        fun newInstance() = HeartReviewFragment()
    }

    override val viewModel by viewModel<HeartReviewViewModel>()
    override fun getViewBinding(): FragmentHeartreviewBinding =
        FragmentHeartreviewBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.heartReviewStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is HeartReviewState.Uninitialized -> {}
                is HeartReviewState.Loading -> {}
                is HeartReviewState.Success -> {

                }

                is HeartReviewState.Error -> {

                }
            }
        }
    }

    override fun initView() = with(binding) {
        toolbarHeartReview.setNavigationOnClickListener {
            finish()
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
}