package com.hand.comeeatme.view.main.home.post

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.post.DetailPostData
import com.hand.comeeatme.databinding.FragmentDetailpostBinding
import com.hand.comeeatme.util.widget.adapter.ViewPagerAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.dialog.PostDialog
import com.hand.comeeatme.view.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class DetailPostFragment : BaseFragment<DetailPostViewModel, FragmentDetailpostBinding>(),
    MainActivity.onBackPressedListener {
    companion object {
        const val POST_ID = "postId"
        const val TAG = "DetailFragment"

        fun newInstance(postId: Long) : DetailPostFragment {
            val bundle = bundleOf(
                POST_ID to postId
            )

            return DetailPostFragment().apply {
                arguments = bundle
            }
        }
    }

    private val postId by lazy {
        arguments?.getLong(POST_ID, -1)
    }

    override val viewModel by viewModel<DetailPostViewModel>{
        parametersOf(
            postId
        )
    }
    override fun getViewBinding(): FragmentDetailpostBinding =
        FragmentDetailpostBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.detailPostStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is DetailPostState.Uninitialized -> {

                }

                is DetailPostState.Loading -> {
                    binding.clLoading.isVisible = true
                }

                is DetailPostState.Success -> {
                    binding.clLoading.isGone = true
                    setView(it.response!!.data)
                }

                is DetailPostState.Error -> {
                    binding.clLoading.isGone = true
                }

            }
        }

    }

    override fun initView() = with(binding) {
        viewModel.getDetailPost()

        tbFollow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // TODO follow 처리
            } else {
                // TODO unFollow 처리
            }
        }

        tbLike.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.likePost(postId!!)
            } else {
                viewModel.unLikePost(postId!!)
            }
        }

        tbBookmark.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.bookmarkPost(postId!!)
            } else {
                viewModel.unBookmarkPost(postId!!)
            }
        }

        toolbarPost.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_Menu -> {
                    // TODO Dialog 띄워서 신고하기 등등 보여주기
                    PostDialog(requireContext()).showDialog()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        toolbarPost.setNavigationOnClickListener {
            finish()
        }

        svPost.setOnRefreshListener {
            viewModel.getDetailPost()
            svPost.isRefreshing = false
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun setView(data: DetailPostData) = with(binding) {
        tvLocation.text = data.restaurant.name

        when (data.imageUrls.size) {
            1 -> {
                ivImageCount.setImageResource(R.drawable.ic_image1_24)
            }
            2 -> {
                ivImageCount.setImageResource(R.drawable.ic_image2_24)
            }
            3 -> {
                ivImageCount.setImageResource(R.drawable.ic_image3_24)
            }
            4 -> {
                ivImageCount.setImageResource(R.drawable.ic_image4_24)
            }
            5 -> {
                ivImageCount.setImageResource(R.drawable.ic_image5_24)
            }
            6 -> {
                ivImageCount.setImageResource(R.drawable.ic_image6_24)
            }
            7 -> {
                ivImageCount.setImageResource(R.drawable.ic_image7_24)
            }
            8 -> {
                ivImageCount.setImageResource(R.drawable.ic_image8_24)
            }
            9 -> {
                ivImageCount.setImageResource(R.drawable.ic_image9_24)
            }
            10 -> {
                ivImageCount.setImageResource(R.drawable.ic_image10_24)
            }
        }

        vpImages.adapter = ViewPagerAdapter(postId!!, data.imageUrls, requireContext(), true)


        if (data.member.imageUrl.isNullOrEmpty()) {
            cvProfile.setImageDrawable(requireContext().getDrawable(R.drawable.food1))
        } else {
            Glide.with(requireContext())
                .load(data.member.imageUrl)
                .into(cvProfile)
        }

        tvNickName.text = data.member.nickname

        // TODO 팔로우 처리

        tbLike.isChecked = data.liked
        tbBookmark.isChecked = data.bookmarked

        tvContent.text = data.content

        // TODO 식당 이미지 처리

        tvRestaurantName.text = data.restaurant.name
        tvAddress.text = data.restaurant.address.name

        flSelectHashTag.removeAllViews()
        for (i in data.hashtags) {
            flSelectHashTag.addItem(viewModel.getHashTagEngToKor()[i]!!)
        }

        tvCommentCount.text = "(${data.commentCount}}"
        tvLikeCount.text = "(${data.likeCount})"

        // TODO 시간 처리
        tvDate.text = data.createdAt
    }

    private fun FlexboxLayout.addItem(tag: String) {

        val chip = LayoutInflater.from(context).inflate(R.layout.layout_chip_custom, null) as Chip

        chip.apply {
            text = "$tag"
            textSize = 14f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            isChecked = false
            checkedIcon = null

            val nonClickBackground = ContextCompat.getColor(context, R.color.white)
            val clickBackground = ContextCompat.getColor(context, R.color.basic)

            chipBackgroundColor = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ),
                intArrayOf(nonClickBackground, clickBackground)
            )

            val nonCLickTextColor = ContextCompat.getColor(context, R.color.basic)
            val clickTextColor = ContextCompat.getColor(context, R.color.white)
            //텍스트
            setTextColor(
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(nonCLickTextColor, clickTextColor)
                )

            )
            isCheckable = false

        }

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )

        layoutParams.rightMargin = dpToPx(10)
        addView(chip, childCount, layoutParams)
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

    private fun dpToPx(dp: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
            .roundToInt()


    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }
}