package com.hand.comeeatme.view.main.user

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.data.response.user.UserDetailData
import com.hand.comeeatme.databinding.FragmentUserBinding
import com.hand.comeeatme.util.widget.adapter.user.UserGridAdapter
import com.hand.comeeatme.util.widget.adapter.user.UserListAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.dialog.UserSortDialog
import com.hand.comeeatme.view.main.user.edit.UserEditFragment
import com.hand.comeeatme.view.main.user.menu.HeartReviewActivity
import com.hand.comeeatme.view.main.user.menu.MyCommentActivity
import com.hand.comeeatme.view.main.user.menu.MyReviewActivity
import com.hand.comeeatme.view.main.user.menu.RecentReviewActivity
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
                    viewModel.getUserDetail()
                }

                is UserState.Loading -> {

                }

                is UserState.UserDetailSuccess -> {
                    viewModel.setProfile(it.response.data.imageUrl)
                    viewModel.setNickname(it.response.data.nickname)
                    setUserInformation(it.response.data)
                    viewModel.getUserPost()
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
        rgListAndGrid.setOnCheckedChangeListener { group, checkId ->
            when (checkId) {
                R.id.rb_Grid -> {
                    binding.rvList.visibility = View.INVISIBLE
                    binding.rvGrid.visibility = View.VISIBLE
                }
                else -> {
                    binding.rvList.visibility = View.VISIBLE
                    binding.rvGrid.visibility = View.INVISIBLE
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
            val intent = Intent(requireContext(), MyReviewActivity::class.java)
            startActivity(intent)
        }

        llRecentReview.setOnClickListener {
            val intent = Intent(requireContext(), RecentReviewActivity::class.java)
            startActivity(intent)
        }

        llMyComment.setOnClickListener {
            val intent = Intent(requireContext(), MyCommentActivity::class.java)
            startActivity(intent)
        }

        llHeartReview.setOnClickListener {
            val intent = Intent(requireContext(), HeartReviewActivity::class.java)
            startActivity(intent)
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUserInformation(data: UserDetailData) = with(binding) {
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
            binding.rvGrid.visibility = View.INVISIBLE
            binding.rvList.visibility = View.INVISIBLE
            binding.clNonPost.visibility = View.VISIBLE
        } else {
            binding.clNonPost.visibility = View.INVISIBLE
            if (binding.rbGrid.isChecked) {
                binding.rvGrid.visibility = View.VISIBLE
                binding.rvList.visibility = View.INVISIBLE
            } else if (binding.rbList.isChecked) {
                binding.rvGrid.visibility = View.INVISIBLE
                binding.rvList.visibility = View.VISIBLE
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