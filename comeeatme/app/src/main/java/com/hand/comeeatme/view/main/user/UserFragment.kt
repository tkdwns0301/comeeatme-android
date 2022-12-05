package com.hand.comeeatme.view.main.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.member.MemberDetailData
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.FragmentUserBinding
import com.hand.comeeatme.util.widget.adapter.user.UserGridAdapter
import com.hand.comeeatme.util.widget.adapter.user.UserListAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.dialog.UserSortDialog
import com.hand.comeeatme.view.main.user.edit.UserEditFragment
import com.hand.comeeatme.view.main.user.menu.heartreview.HeartReviewFragment
import com.hand.comeeatme.view.main.user.menu.mycomment.MyCommentFragment
import com.hand.comeeatme.view.main.user.menu.myreview.MyReviewFragment
import com.hand.comeeatme.view.main.user.menu.recentreview.RecentReviewFragment
import com.hand.comeeatme.view.main.user.setting.SettingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : BaseFragment<UserViewModel, FragmentUserBinding>() {
    companion object {
        fun newInstance() = UserFragment()
        const val TAG = "UserFragment"
    }

    override val viewModel by viewModel<UserViewModel>()
    override fun getViewBinding(): FragmentUserBinding = FragmentUserBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.userStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is UserState.Uninitialized -> {
                    viewModel.getMemberDetail()
                }

                is UserState.Loading -> {

                }

                is UserState.UserDetailSuccess -> {
                    viewModel.setProfile(it.response.data.imageUrl)
                    viewModel.setNickname(it.response.data.nickname)
                    setUserInformation(it.response.data)
                    viewModel.getMemberPost()
                }

                is UserState.UserPostSuccess -> {
                    setUserPost(it.response.data.content)
                }

                is UserState.Error -> {

                }
            }
        }
    }

    private lateinit var adapterList: UserListAdapter
    private lateinit var adapterGrid: UserGridAdapter

    override fun initView() = with(binding) {
        ibSetting.setOnClickListener {
            startActivity(SettingActivity.newIntent(requireContext()))
        }

        rgListAndGrid.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.rb_Grid -> {
                    binding.rvList.isGone = true
                    binding.rvGrid.isVisible = true
                }
                else -> {
                    binding.rvList.isVisible = true
                    binding.rvGrid.isGone = true
                }
            }

        }

        ivName.setOnClickListener {
            val manager: FragmentManager = requireActivity().supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, UserEditFragment.newInstance(viewModel.getProfile(), viewModel.getNickname(), viewModel.getIntroduction()), UserEditFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        clSort.setOnClickListener {
            UserSortDialog(requireContext()).showDialog()
        }

        clFollower.setOnClickListener {
            val intent = Intent(requireContext(), FollowActivity::class.java)
            intent.putExtra("isFollowerView", true)
            startActivity(intent)
        }

        clFollowing.setOnClickListener {
            val intent = Intent(requireContext(), FollowActivity::class.java)
            intent.putExtra("isFollowerView", false)
            startActivity(intent)
        }

        llMyReview.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer,
                MyReviewFragment.newInstance(),
                MyReviewFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        llRecentReview.setOnClickListener {
            val manager: FragmentManager = requireActivity().supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, RecentReviewFragment.newInstance(), RecentReviewFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        llMyComment.setOnClickListener {
            val manager: FragmentManager = requireActivity().supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, MyCommentFragment.newInstance(), MyCommentFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        llHeartReview.setOnClickListener {
            val manager: FragmentManager = requireActivity().supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, HeartReviewFragment.newInstance(), HeartReviewFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        srlUser.setOnRefreshListener {
            viewModel.getMemberDetail()
            srlUser.isRefreshing = false
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUserInformation(data: MemberDetailData) = with(binding) {
        if (data.imageUrl.isNullOrEmpty()) {
            clProfile.setImageDrawable(requireContext().getDrawable(R.drawable.food1))
        } else {
            Glide.with(requireContext())
                .load(data.imageUrl)
                .into(clProfile)
        }

        viewModel.setIntroduction(data.introduction)

        tvName.text = data.nickname
        tvIntroduce.text = data.introduction
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUserPost(contents: List<Content>) {
        if (contents.isNullOrEmpty()) {
            binding.rvGrid.isGone = true
            binding.rvList.isGone = true
            binding.clNonPost.isVisible = true
        } else {
            binding.clNonPost.isGone = true
            if (binding.rbGrid.isChecked) {
                binding.rvGrid.isVisible = true
                binding.rvList.isGone = true
            } else if (binding.rbList.isChecked) {
                binding.rvGrid.isGone = true
                binding.rvList.isVisible = true
            }
            adapterGrid = UserGridAdapter(contents, requireContext())
            adapterList = UserListAdapter(contents, requireContext(),
                viewModel.getProfile(), viewModel.getNickname(),
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
                })

            binding.rvGrid.adapter = adapterGrid
            binding.rvList.adapter = adapterList

            adapterGrid.notifyDataSetChanged()
            adapterList.notifyDataSetChanged()
        }
    }

}