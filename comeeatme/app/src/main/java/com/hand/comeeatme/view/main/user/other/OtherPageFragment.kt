package com.hand.comeeatme.view.main.user.other

import android.annotation.SuppressLint
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.member.MemberDetailData
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.FragmentOtherPageBinding
import com.hand.comeeatme.util.widget.adapter.user.UserGridAdapter
import com.hand.comeeatme.util.widget.adapter.user.UserListAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.dialog.RestaurantPostSortDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtherPageFragment : BaseFragment<OtherPageViewModel, FragmentOtherPageBinding>() {
    companion object {
        const val TAG = "OtherPageFragment"
        const val MEMBER_ID = "memberId"

        fun newInstance(memberId: Long): OtherPageFragment {
            val bundle = bundleOf(
                MEMBER_ID to memberId
            )

            return OtherPageFragment().apply {
                arguments = bundle
            }
        }
    }

    private val memberId by lazy {
        arguments?.getLong(MEMBER_ID, -1)
    }

    private lateinit var adapterGrid: UserGridAdapter
    private lateinit var adapterList: UserListAdapter

    override val viewModel by viewModel<OtherPageViewModel>()
    override fun getViewBinding(): FragmentOtherPageBinding =
        FragmentOtherPageBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.otherPageStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is OtherPageState.Uninitialized -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    viewModel.getDetailMember(memberId!!)
                }

                is OtherPageState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is OtherPageState.Success -> {
                    viewModel.setProfile(it.response.data.imageUrl)
                    viewModel.setNickname(it.response.data.nickname)
                    setUserInformation(it.response.data)
                    viewModel.getMemberPost(true, memberId!!)
                }

                is OtherPageState.MemberPostSuccess -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setMemberPost(it.response.data!!.content)
                }

                is OtherPageState.Error -> {
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

        rgListAndGrid.setOnCheckedChangeListener {_, checkid ->
            when(checkid) {
                R.id.rb_Grid -> {
                    rvList.isGone = true
                    rvGrid.isVisible = true
                } else -> {
                    rvList.isVisible = true
                    rvGrid.isGone = true
                }
            }
        }

        ibPrev.setOnClickListener {
            finish()
        }

        srlOtherPage.setOnRefreshListener {
            viewModel.getDetailMember(memberId!!)
            refresh()
        }

        clSort.setOnClickListener {
            RestaurantPostSortDialog(
                requireContext(),
                recentSort = {
                    viewModel.setSort(it)
                    viewModel.getMemberPost(true, memberId!!)
                },
                likeSort = {
                    viewModel.setSort(it)
                    viewModel.getMemberPost(true, memberId!!)
                }
            ).show()
        }

        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!viewModel.getIsLast()) {
                    if (!binding.rvList.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getMemberPost(false, memberId!!)
                    }
                }
            }
        })

        rvGrid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!viewModel.getIsLast()) {
                    if (!binding.rvGrid.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getMemberPost(false, memberId!!)
                    }
                }
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUserInformation(data: MemberDetailData) = with(binding) {
        if (data.imageUrl.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(R.drawable.default_profile)
                .into(civProfile)
        } else {
            Glide.with(requireContext())
                .load(data.imageUrl)
                .into(civProfile)
        }

        tvNickname.text = data.nickname
        tvIntroduction.text = data.introduction
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setMemberPost(contents: List<Content>) = with(binding) {
        if (contents.isNullOrEmpty()) {
            rvGrid.isGone = true
            rvList.isGone = true
            clNonPost.isVisible = true
        } else {
            clNonPost.isGone = true
            if (rbGrid.isChecked) {
                rvGrid.isVisible = true
                rvList.isGone = true
            } else if (rbList.isChecked) {
                rvGrid.isGone = true
                rvList.isVisible = true
            }

            val recyclerViewState1 = binding.rvGrid.layoutManager?.onSaveInstanceState()
            val recyclerViewState2 = binding.rvList.layoutManager?.onSaveInstanceState()

            adapterGrid = UserGridAdapter(contents, requireContext())
            adapterList = UserListAdapter(
                contents,
                requireContext(),
                viewModel.getProfile(),
                viewModel.getNickname()!!,
                likePost = {
                    viewModel.likePost(it)
                },
                unLikePost = {
                    viewModel.unLikePost(it)
                },
                bookmarkPost = {
                    viewModel.bookmarkPost(it)
                },
                unBookmarkPost = {
                    viewModel.unBookmarkPost(it)
                }
            )

            rvGrid.adapter = adapterGrid
            rvList.adapter = adapterList
            rvGrid.layoutManager?.onRestoreInstanceState(recyclerViewState1)
            rvList.layoutManager?.onRestoreInstanceState(recyclerViewState2)

            adapterGrid.notifyDataSetChanged()
            adapterList.notifyDataSetChanged()
        }
    }

    private fun refresh() {
        viewModel.getDetailMember(memberId!!)
        binding.srlOtherPage.isRefreshing = false
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