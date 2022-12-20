package com.hand.comeeatme.view.main.user.menu.likepost

import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.FragmentLikedpostBinding
import com.hand.comeeatme.util.widget.adapter.user.LikedPostAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LikedPostFragment : BaseFragment<LikedPostViewModel, FragmentLikedpostBinding>() {
    companion object {
        const val TAG = "LikePostFragment"

        fun newInstance() = LikedPostFragment()
    }

    override val viewModel by viewModel<LikedPostViewModel>()
    override fun getViewBinding(): FragmentLikedpostBinding =
        FragmentLikedpostBinding.inflate(layoutInflater)

    private lateinit var adapter : LikedPostAdapter

    override fun observeData() {
        viewModel.likedPostStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is LikedPostState.Uninitialized -> {}
                is LikedPostState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
                is LikedPostState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setAdapter(it.response.data!!.content)
                }

                is LikedPostState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initView() = with(binding) {
        rvHeartReview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        toolbarLikePost.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setAdapter(contents: List<Content>) {
        adapter = LikedPostAdapter(
            requireContext(),
            contents,
        )

        binding.rvHeartReview.adapter = adapter
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