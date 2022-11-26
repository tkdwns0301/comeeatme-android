package com.hand.comeeatme.view.main.user.menu.mycomment

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hand.comeeatme.databinding.FragmentMycommentBinding
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyCommentFragment: BaseFragment<MyCommentViewModel, FragmentMycommentBinding>() {
    companion object {
        const val TAG = "MyCommentFragment"

        fun newInstance() = MyCommentFragment()
    }

    override val viewModel by viewModel<MyCommentViewModel>()
    override fun getViewBinding(): FragmentMycommentBinding = FragmentMycommentBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.myCommentStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is MyCommentState.Uninitialized ->{

                }

                is MyCommentState.Loading -> {

                }

                is MyCommentState.Success -> {

                }

                is MyCommentState.Error -> {

                }
            }

        }
    }


    override fun initView() = with(binding) {
        toolbarMyComment.setNavigationOnClickListener {
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