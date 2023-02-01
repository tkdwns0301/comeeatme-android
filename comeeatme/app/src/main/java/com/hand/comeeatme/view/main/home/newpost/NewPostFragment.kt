package com.hand.comeeatme.view.main.home.newpost

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.post.DetailPostData
import com.hand.comeeatme.databinding.FragmentNewpostBinding
import com.hand.comeeatme.util.widget.adapter.NewPostPhotosAdapter
import com.hand.comeeatme.util.widget.adapter.SearchNewPostRestaurantAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.main.home.newpost.album.AlbumActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.math.roundToInt


class NewPostFragment : BaseFragment<NewPostViewModel, FragmentNewpostBinding>() {
    companion object {
        const val IS_MODIFY = "isModify"
        const val POST_ID = "postId"
        const val TAG = "NewPostFragment"

        fun newInstance(isModify: Boolean?, postId: Long?): NewPostFragment {
            val bundle = bundleOf(
                IS_MODIFY to isModify,
                POST_ID to postId
            )

            return NewPostFragment().apply {
                arguments = bundle
            }
        }
    }

    override val viewModel by viewModel<NewPostViewModel>()
    override fun getViewBinding(): FragmentNewpostBinding =
        FragmentNewpostBinding.inflate(layoutInflater)

    private val isModify by lazy {
        arguments?.getBoolean(IS_MODIFY, false)
    }

    private val postId by lazy {
        arguments?.getLong(POST_ID, -1)
    }

    private lateinit var photoAdapter: NewPostPhotosAdapter
    private lateinit var restaurantAdapter: SearchNewPostRestaurantAdapter

    private var images = ArrayList<Uri>()
    private var checkedImageList = ArrayList<String>()
    private var imagePositionList = ArrayList<Int>()
    private var cropImageList = ArrayList<String>()


    private val REQ_STORAGE_PERMISSION = 201
    private val moodList: Array<String> = arrayOf("혼밥", "데이트", "단체모임", "특별한 날", "감성있는")
    private val priceList: Array<String> = arrayOf("가성비", "자극적인", "고급스러운", "신선한 재료", "시그니쳐 메뉴")
    private val etcList: Array<String> = arrayOf("친절", "청결", "반려동물 동반", "아이 동반", "24시간", "주차장")

    private val selectPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.getStringArrayListExtra(NewPostViewModel.COMPRESSED_PHOTO_KEY)?.let {
                Log.e("NewPost Compress", "$it")
                viewModel.setResultPhotoList(it)
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun observeData() {
        viewModel.newPostStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NewPostState.Uninitialized -> {
                    if (isModify == true) {
                        viewModel.getDetailPost(postId!!)
                    }
                }

                is NewPostState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is NewPostState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    finish()
                }

                is NewPostState.CompressPhotoFinish -> {
                    setImageAdapter(it.compressPhotoList, false)
                }

                is NewPostState.NewPostReady -> {
                    binding.tvFinish.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.basic))
                }

                is NewPostState.NewPostUnReady -> {
                    binding.tvFinish.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.nonCheck))
                }

                is NewPostState.SearchRestaurantSuccess -> {
                    restaurantAdapter = SearchNewPostRestaurantAdapter(
                        requireContext(),
                        it.restaurants,
                        false,
                        onClickItem = {
                            viewModel.setRestaurantId(it.id)
                            binding.tvSelectedLocation.text = it.name
                            binding.icLocation.clLocation.isGone = true
                            binding.ivLocation.setImageResource(R.drawable.ic_arrow_down_green_32)
                        }
                    )
                    binding.icLocation.rvLocationResult.adapter = restaurantAdapter
                }

                is NewPostState.DetailPostSuccess -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setView(it.response.data)
                }

                is NewPostState.ModifyPostSuccess -> {
                    finish()
                }

                is NewPostState.Error -> {

                }
            }
        }

    }

    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        rvSelectedImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        icLocation.rvLocationResult.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        for (mood in moodList) {
            icHashTag.flMood.addItem(mood, false)
        }

        for (price in priceList) {
            icHashTag.flPrice.addItem(price, false)
        }

        for (etc in etcList) {
            icHashTag.flEtc.addItem(etc, false)
        }

        clImageSelect.setOnClickListener {
            val readPermission = ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (readPermission == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQ_STORAGE_PERMISSION
                )
            } else {
                selectPhotoLauncher.launch(
                    AlbumActivity.newIntent(requireContext(), false)
                )

            }
        }

        tvFinish.setOnClickListener {
            if (tvFinish.currentTextColor == ContextCompat.getColor(requireContext(),
                    R.color.nonCheck)
            ) {
                Toast.makeText(requireContext(), "모든 항목을 채워주셔야 합니다.", Toast.LENGTH_SHORT).show()
            } else if (tvFinish.currentTextColor == ContextCompat.getColor(requireContext(),
                    R.color.basic)
            ) {
                if (isModify == false) {
                    viewModel.getImageIds()
                } else {
                    viewModel.modifyPost(postId!!)
                }

            }
        }

        ibPrev.setOnClickListener {
            finish()
        }

        llLocation.setOnClickListener {
            if (!icLocation.clLocation.isVisible) {
                icLocation.clLocation.isVisible = true
            } else if (icLocation.clLocation.isVisible) {
                icLocation.clLocation.isGone = true

                if (tvSelectedLocation.text.isEmpty()) {
                    ivLocation.setImageResource(R.drawable.ic_arrow_down_32_gray)
                } else {
                    ivLocation.setImageResource(R.drawable.ic_arrow_down_green_32)
                }

            }
        }

        llHashTag.setOnClickListener {
            if (!icHashTag.clHashTag.isVisible) {
                icHashTag.clHashTag.isVisible = true
            } else if (icHashTag.clHashTag.isVisible) {
                icHashTag.clHashTag.isGone = true
            }
        }

        icLocation.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (icLocation.etSearch.text.isEmpty()) {
                    icLocation.ibSearchDelete.isGone = true
                } else {
                    if (!icLocation.ibSearchDelete.isVisible) {
                        icLocation.ibSearchDelete.isVisible = true
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        icLocation.ibSearchDelete.setOnClickListener {
            binding.icLocation.etSearch.text = null
        }

        icLocation.ibSearch.setOnClickListener {
            val search: String = icLocation.etSearch.text.toString()
            viewModel.setQuery(search)
            viewModel.searchRestaurants(true)
        }

        icLocation.etSearch.setOnKeyListener { p0, p1, p2 ->
            when (p1) {
                KeyEvent.KEYCODE_ENTER -> {
                    val search: String = icLocation.etSearch.text.toString()
                    viewModel.setQuery(search)
                    viewModel.searchRestaurants(true)
                }

            }

            true
        }

        etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (etContent.text.isEmpty()) {
                    viewModel.removeContent()
                } else {
                    viewModel.setContent("${etContent.text}")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        icHashTag.tvInit.setOnClickListener {
            viewModel.clearHashTag()
        }

        icLocation.rvLocationResult.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!viewModel.getIsLast()) {
                    if(!icLocation.rvLocationResult.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.searchRestaurants(false)
                    }
                }
            }
        })
    }

    private fun setView(data: DetailPostData) = with(binding) {
        tvSelectedLocation.text = data.restaurant.name
        viewModel.setRestaurantId(data.restaurant.id)

        clImageSelect.isGone = true

        viewModel.getChipList().forEach { chip ->
            if (data.hashtags.contains(viewModel.hashTagKorToEng("${chip.text}"))) {
                chip.isChecked = true
            }
        }

        setImageAdapter(data.imageUrls as ArrayList<String>, true)
        viewModel.setResultPhotoList(data.imageUrls)

        etContent.setText(data.content)
        viewModel.setContent(data.content)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 100) {
            checkedImageList = data!!.getStringArrayListExtra("checkedImages") as ArrayList<String>
            imagePositionList = data!!.getIntegerArrayListExtra("imagePosition") as ArrayList<Int>
            cropImageList = data!!.getStringArrayListExtra("cropImages") as ArrayList<String>

            images = ArrayList()

            cropImageList!!.forEach {
                images.add(Uri.fromFile(File(it)))
            }

        }
    }


    private fun FlexboxLayout.addItem(tag: String, isSelected: Boolean) {

        val chip = LayoutInflater.from(context).inflate(R.layout.layout_chip_custom, null) as Chip

        chip.apply {
            text = "$tag"
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            isChecked = false
            checkedIcon = null


            if (!isSelected) {
                textSize = 16f
                val nonClickBackground = ContextCompat.getColor(context, R.color.whiteF)
                val clickBackground = ContextCompat.getColor(context, R.color.basic)

                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(nonClickBackground, clickBackground)
                )

                val nonClickStroke = ContextCompat.getColor(context, R.color.basic68)
                val clickStroke = ContextCompat.getColor(context, R.color.transparent)

                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(nonClickStroke, clickStroke)
                )

                val nonCLickTextColor = ContextCompat.getColor(context, R.color.basic68)
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

                isCheckable = true

            } else {
                textSize = 14f
                val nonClickBackground = ContextCompat.getColor(context, R.color.white)
                val clickBackground = ContextCompat.getColor(context, R.color.basic)

                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(nonClickBackground, clickBackground)
                )

                val nonClickStroke = ContextCompat.getColor(context, R.color.basic68)
                val clickStroke = ContextCompat.getColor(context, R.color.transparent)

                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(nonClickStroke, clickStroke)
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


            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    binding.flSelectHashTag.addItem("#$tag", true)
                    viewModel.addHashTag(tag, chip)
                    if(viewModel.getCheckedChipList().isNotEmpty()) {
                        binding.ivHashTag.setImageResource(R.drawable.ic_arrow_down_green_32)
                    }

                } else {
                    val chipIndex = viewModel.getCheckedChipList().indexOf(chip)
                    binding.flSelectHashTag.removeViewAt(chipIndex)
                    viewModel.removeHashTag(tag, chip)

                    if(viewModel.getCheckedChipList().isEmpty()) {
                        binding.ivHashTag.setImageResource(R.drawable.ic_arrow_down_32_gray)
                    }
                }
            }
        }

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )

        layoutParams.rightMargin = dpToPx(4)
        addView(chip, childCount, layoutParams)

        if (!isSelected) {
            viewModel.addChip(chip)
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setImageAdapter(data: ArrayList<String>, isModify: Boolean) {
        photoAdapter = NewPostPhotosAdapter(
            requireContext(),
            data,
            isModify
        )
        binding.rvSelectedImages.adapter = photoAdapter
        photoAdapter.notifyDataSetChanged()
    }


    private fun finish() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        val manager: FragmentManager? = activity?.supportFragmentManager
        val ft: FragmentTransaction = manager!!.beginTransaction()

        val newPost = manager.findFragmentByTag(TAG)

        if (newPost != null) {
            ft.remove(newPost)
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

}

