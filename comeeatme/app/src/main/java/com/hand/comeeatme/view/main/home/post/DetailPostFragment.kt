package com.hand.comeeatme.view.main.home.post


import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.comment.CommentListContent
import com.hand.comeeatme.data.response.post.DetailPostData
import com.hand.comeeatme.databinding.FragmentDetailpostBinding
import com.hand.comeeatme.util.DynamicLinkUtils
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
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt


class DetailPostFragment : BaseFragment<DetailPostViewModel, FragmentDetailpostBinding>() {
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
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    viewModel.getDetailPost()
                    viewModel.getCommentList(true)
                }

                is DetailPostState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is DetailPostState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    viewModel.setPostWriterMemberId(it.response!!.data.member.id)
                    viewModel.setRestaurantId(it.response.data.restaurant.id)
                    viewModel.getRestaurantImage(it.response.data.restaurant.id)
                    setView(it.response.data)
                }

                is DetailPostState.RestaurantImageSuccess -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Glide.with(requireContext())
                        .load(it.response!!.data.content[0].imageUrl)
                        .into(binding.ivRestaurantImage)
                }

                is DetailPostState.CommentListSuccess -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setAdapter(it.response!!)
                }

                is DetailPostState.WritingCommentSuccess -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    viewModel.getCommentList(true)
                }

                is DetailPostState.DeletePostSuccess -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    finish()
                }

                is DetailPostState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private lateinit var adapter: CommentListAdapter

    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        etComment.requestFocus()

        keyboardVisibilityUtil = KeyboardVisibilityUtil(requireActivity().window, onShowKeyboard = {
            Log.e("keyboard", "open")
        }, onHideKeyboard = {
            if (etComment.text.isEmpty()) {
                etComment.hint = "댓글을 입력해주세요."
                parentId = null
            }

        })

        rvCommentList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        civProfile.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer,
                OtherPageFragment.newInstance(viewModel.getMemberId()),
                OtherPageFragment.TAG)
            ft.addToBackStack(OtherPageFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        tvNickName.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer,
                OtherPageFragment.newInstance(viewModel.getMemberId()),
                OtherPageFragment.TAG)
            ft.addToBackStack(OtherPageFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        tbFollow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // TODO follow 처리
            } else {
                // TODO unFollow 처리
            }
        }

        tbLike.setOnClickListener {
            if (tbLike.isChecked) {
                viewModel.likePost(postId!!)
            } else {
                viewModel.unLikePost(postId!!)
            }
        }

        tbBookmark.setOnClickListener {
            if (tbBookmark.isChecked) {
                viewModel.bookmarkPost(postId!!)
            } else {
                viewModel.unBookmarkPost(postId!!)
            }
        }

        toolbarPost.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_Menu -> {
                    if (viewModel.getPostWriterMemberId() == viewModel.getMemberId()) {
                        MyPostDialog(
                            requireContext(),
                            modifyPost = {
                                val manager: FragmentManager =
                                    requireActivity().supportFragmentManager
                                val ft: FragmentTransaction = manager.beginTransaction()

                                ft.add(R.id.fg_MainContainer,
                                    NewPostFragment.newInstance(true, postId),
                                    NewPostFragment.TAG)
                                ft.commitAllowingStateLoss()
                            },
                            deletePost = {
                                viewModel.deletePost()
                            },
                            dynamicLink = {
                                DynamicLinkUtils.onDynamicLinkClick(
                                    requireActivity(),
                                    MainActivity.SCHEME_POSTID,
                                    MainActivity.PARAM_ID,
                                    "$postId"
                                )
                            }
                        ).show()
                    } else {
                        OtherPostDialog(
                            requireContext(),
                            postId!!,
                            dynamicLink = {
                                DynamicLinkUtils.onDynamicLinkClick(
                                    requireActivity(),
                                    MainActivity.SCHEME_POSTID,
                                )
                            },
                        ).show()
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

                val inputMethodManager =
                    requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(etComment.windowToken, 0)
            }
        }

        svPost.setOnRefreshListener {
            viewModel.getDetailPost()
            svPost.isRefreshing = false
        }

        etComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (etComment.text.isEmpty()) {
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

            val findFragment =
                activity?.supportFragmentManager?.findFragmentByTag(DetailRestaurantFragment.TAG)

            findFragment?.let {
                manager.beginTransaction().remove(it).commitAllowingStateLoss()
            }

            ft.add(R.id.fg_MainContainer,
                DetailRestaurantFragment.newInstance(viewModel.getRestaurantId()!!),
                DetailRestaurantFragment.TAG)
            ft.addToBackStack(DetailRestaurantFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        rvCommentList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!viewModel.getIsLast()) {
                    if (rvCommentList.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getCommentList(false)
                    }
                }
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: ArrayList<CommentListContent>) {
        adapter = CommentListAdapter(
            contents,
            requireContext(),
            viewModel.getMemberId(),
            sendParentId = { parentId, nickname ->
                this.parentId = parentId
                Log.e("parentId", "${this.parentId}")
                binding.etComment.hint = "${nickname}님에게 다실 답글을 입력해주세요."

                binding.slPost.scrollToView(binding.etComment)

                val inputMethodManager =
                    requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
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

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "SimpleDateFormat")
    private fun setView(data: DetailPostData) = with(binding) {
        tvLocation.text = data.restaurant.name

        val imageCounts = arrayListOf<Int>(
            R.drawable.ic_image1_24,
            R.drawable.ic_image2_24,
            R.drawable.ic_image3_24,
            R.drawable.ic_image4_24,
            R.drawable.ic_image5_24,
            R.drawable.ic_image6_24,
            R.drawable.ic_image7_24,
            R.drawable.ic_image8_24,
            R.drawable.ic_image9_24,
            R.drawable.ic_image10_24,
        )

        Log.e("imageCount", "${imageCounts.size}")
        Log.e("size", "${data.imageUrls.size}")

        Glide.with(requireContext())
            .load(imageCounts[data.imageUrls.size - 1])
            .into(ivImageCount)


        vpImages.adapter = ViewPagerAdapter(postId!!, data.imageUrls, requireContext(), true)


        if (data.member.imageUrl.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(R.drawable.default_profile)
                .into(civProfile)
        } else {
            Glide.with(requireContext())
                .load(data.member.imageUrl)
                .into(civProfile)
        }

        tvNickName.text = data.member.nickname

        tbLike.isChecked = data.liked
        tbBookmark.isChecked = data.bookmarked

        tvContent.text = data.content


        tvRestaurantName.text = data.restaurant.name
        tvAddress.text = data.restaurant.address.name

        flSelectHashTag.removeAllViews()
        for (i in data.hashtags) {
            flSelectHashTag.addItem(viewModel.getHashTagEngToKor()[i]!!)
        }

        tvCommentCount.text = "(${data.commentCount})"
        tvLikeCount.text = "(${data.likeCount})"

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        val createdTime = sdf.parse(data.createdAt)
        val createdMillis = createdTime.time

        val currMillis = System.currentTimeMillis()

        var diff = (currMillis - createdMillis)

        when {
            diff < 60000 -> {
                tvDate.text = "방금 전"
            }
            diff < 3600000 -> {
                tvDate.text = "${TimeUnit.MILLISECONDS.toMinutes(diff)}분 전"
            }
            diff < 86400000 -> {
                tvDate.text = "${TimeUnit.MILLISECONDS.toHours(diff)}시간 전"
            }
            diff < 604800000 -> {
                tvDate.text = "${TimeUnit.MILLISECONDS.toDays(diff)}일 전"
            }
            diff < 2419200000 -> {
                tvDate.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 7}주 전"
            }
            diff < 31556952000 -> {
                tvDate.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 30}개월 전"
            }
            else -> {
                tvDate.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 365}년 전"
            }
        }
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

    @SuppressLint("SetTextI18n", "CutPasteId")
    private fun FlexboxLayout.addItem(tag: String) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_hashtag_uncheck, null) as ConstraintLayout

        view.findViewById<TextView>(R.id.tv_HashTag).text = "#$tag"
        view.findViewById<TextView>(R.id.tv_HashTag).textSize = 14f

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )

        layoutParams.rightMargin = dpToPx(4)
        layoutParams.bottomMargin = dpToPx(4)
        addView(view, childCount, layoutParams)
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



    override fun onDestroy() {
        keyboardVisibilityUtil.detachKeyboardListeners()
        super.onDestroy()
    }
}