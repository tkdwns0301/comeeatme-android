package com.hand.comeeatme.view.main.home.post


import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.comment.CommentListContent
import com.hand.comeeatme.data.response.post.DetailPostData
import com.hand.comeeatme.databinding.FragmentDetailpostBinding
import com.hand.comeeatme.util.KeyboardVisibilityUtil
import com.hand.comeeatme.util.widget.adapter.comment.CommentListAdapter
import com.hand.comeeatme.util.widget.adapter.home.ViewPagerAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.dialog.MyPostDialog
import com.hand.comeeatme.view.dialog.OtherPostDialog
import com.hand.comeeatme.view.main.MainActivity
import com.hand.comeeatme.view.main.home.newpost.NewPostFragment
import com.hand.comeeatme.view.main.rank.restaurant.DetailRestaurantFragment
import com.hand.comeeatme.view.main.user.other.OtherPageFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs
import kotlin.math.roundToInt


class DetailPostFragment : BaseFragment<DetailPostViewModel, FragmentDetailpostBinding>(),
    MainActivity.onBackPressedListener {
    companion object {
        const val POST_ID = "postId"
        const val TAG = "DetailFragment"

        fun newInstance(postId: Long): DetailPostFragment {
            val bundle = bundleOf(
                POST_ID to postId
            )

            return DetailPostFragment().apply {
                arguments = bundle
            }
        }
    }

    private var parentId: Long? = null
    private lateinit var keyboardVisibilityUtil: KeyboardVisibilityUtil

    private val postId by lazy {
        arguments?.getLong(POST_ID, -1)
    }

    override val viewModel by viewModel<DetailPostViewModel> {
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
                    viewModel.getDetailPost()
                    viewModel.getCommentList(0, 10, false)
                }

                is DetailPostState.Loading -> {
                    binding.clLoading.isVisible = true
                }

                is DetailPostState.Success -> {
                    binding.clLoading.isGone = true
                    viewModel.setPostWriterMemberId(it.response!!.data.member.id)
                    viewModel.setRestaurantId(it.response.data.restaurant.id)
                    viewModel.getRestaurantImage(it.response.data.restaurant.id)
                    setView(it.response.data)
                }

                is DetailPostState.RestaurantImageSuccess -> {
                    binding.clLoading.isGone = true
                    Glide.with(requireContext())
                        .load(it.response!!.data.content[0].imageUrl)
                        .into(binding.ivRestaurantImage)
                }

                is DetailPostState.CommentListSuccess -> {
                    binding.clLoading.isGone = true
                    setAdapter(it.response!!.data.content)
                }

                is DetailPostState.WritingCommentSuccess -> {
                    viewModel.getDetailPost()
                    viewModel.getCommentList(0, 10, false)
                }

                is DetailPostState.DeletePostSuccess -> {
                    finish()
                }

                is DetailPostState.Error -> {
                    binding.clLoading.isGone = true
                }

            }
        }

    }

    private lateinit var adapter: CommentListAdapter

    override fun initView() = with(binding) {
        etComment.requestFocus()

        keyboardVisibilityUtil = KeyboardVisibilityUtil(requireActivity().window, onShowKeyboard = {
            Log.e("keyboard", "open")
        }, onHideKeyboard = {
            if(etComment.text.isEmpty()) {
                etComment.hint = "댓글을 입력해주세요."
                parentId = null
            }

        })

        rvCommentList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        clProfileNameFollow.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, OtherPageFragment.newInstance(viewModel.getMemberId()), OtherPageFragment.TAG)
            ft.commitAllowingStateLoss()
        }

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
                    if(viewModel.getPostWriterMemberId() == viewModel.getMemberId()) {
                        MyPostDialog(
                            requireContext(),
                            modifyPost = {
                                // TODO 정보도 같이 넘겨주기 (사진은 안바뀌게)
                                val manager: FragmentManager = requireActivity().supportFragmentManager
                                val ft: FragmentTransaction = manager.beginTransaction()

                                ft.add(R.id.fg_MainContainer, NewPostFragment.newInstance(true, postId), NewPostFragment.TAG)
                                ft.commitAllowingStateLoss()
                            },
                            deletePost = {
                                viewModel.deletePost()
                            }
                        ).show()
                    } else {
                        OtherPostDialog(requireContext()).show()
                    }

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

        ibComment.setOnClickListener {
            if (etComment.text.isNotBlank()) {
                Log.e("send: parendId", "${parentId}")
                viewModel.writingComment(parentId, etComment.text.toString())
                etComment.text = null
                parentId = null
                etComment.hint = "댓글을 입력해 주세요."

                val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(etComment.windowToken, 0)
            }
        }

        svPost.setOnRefreshListener {
            viewModel.getDetailPost()
            svPost.isRefreshing = false
        }

        etComment.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(etComment.text.isEmpty()) {
                    ibComment.setImageResource(R.drawable.ic_comment_send_unchecked_32)
                } else {
                    ibComment.setImageResource(R.drawable.ic_comment_send_checked_32)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        clStoreImageAndNameAndLocation.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer,
                DetailRestaurantFragment.newInstance(viewModel.getRestaurantId()!!),
                DetailRestaurantFragment.TAG)
            ft.commitAllowingStateLoss()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<CommentListContent>) {
        adapter = CommentListAdapter(
            contents,
            requireContext(),
            viewModel.getMemberId(),
            sendParentId = { parentId, nickname ->
                this.parentId = parentId
                Log.e("parentId", "${this.parentId}")
                binding.etComment.hint = "${nickname}님에게 다실 답글을 입력해주세요."

                binding.slPost.scrollToView(binding.etComment)

                val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(binding.etComment, 0)
            },
            modifyComment = {
                // 답글 수정 로직 생각하자
            },
            deleteComment = {
                viewModel.deleteComment(it)
            }
        )

        binding.rvCommentList.adapter = adapter
        adapter.notifyDataSetChanged()
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

        tvCommentCount.text = "(${data.commentCount})"
        tvLikeCount.text = "(${data.likeCount})"

        // TODO 시간 처리
        tvDate.text = data.createdAt
    }

    private fun NestedScrollView.scrollToView(view: View) {
        val y = computeDistanceToView(view)
        this.smoothScrollTo(0, y)
    }

    private fun NestedScrollView.computeDistanceToView(view: View): Int {
        return abs(calculateRectOnScreen(this).top - (this.scrollY + calculateRectOnScreen(view).top) + 50)
    }

    private fun calculateRectOnScreen(view: View): Rect {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return Rect(
            location[0],
            location[1],
            location[0] + view.measuredWidth,
            location[1] + view.measuredHeight
        )
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

    override fun onDestroy() {
        keyboardVisibilityUtil.detachKeyboardListeners()
        super.onDestroy()
    }
}