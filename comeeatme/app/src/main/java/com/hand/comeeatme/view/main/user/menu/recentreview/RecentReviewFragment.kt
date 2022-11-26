package com.hand.comeeatme.view.main.user.menu.recentreview

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hand.comeeatme.databinding.FragmentRecentreviewBinding
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecentReviewFragment : BaseFragment<RecentReviewViewModel, FragmentRecentreviewBinding>() {
    companion object {
        const val TAG = "RecentReviewFragment"

        fun newInstance() = RecentReviewFragment()
    }

    override val viewModel by viewModel<RecentReviewViewModel>()
    override fun getViewBinding(): FragmentRecentreviewBinding =
        FragmentRecentreviewBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.recentReviewStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is RecentReviewState.Uninitialized -> {

                }

                is RecentReviewState.Loading -> {

                }

                is RecentReviewState.Success -> {

                }

                is RecentReviewState.Error -> {

                }


            }
        }
    }

    override fun initView() = with(binding) {
        toolbarRecentReview.setNavigationOnClickListener {
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