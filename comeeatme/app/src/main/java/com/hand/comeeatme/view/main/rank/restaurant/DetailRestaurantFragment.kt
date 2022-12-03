package com.hand.comeeatme.view.main.rank.restaurant

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.image.RestaurantImageContent
import com.hand.comeeatme.data.response.restaurant.DetailRestaurantData
import com.hand.comeeatme.databinding.FragmentDetailRestaurantBinding
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class DetailRestaurantFragment :
    BaseFragment<DetailRestaurantViewModel, FragmentDetailRestaurantBinding>() {
    companion object {
        const val TAG = "DetailRestaurantFragment"
        const val RESTAURANT_ID = "restaurantId"
        fun newInstance(restaurantId: Long): DetailRestaurantFragment {
            val bundle = bundleOf(
                RESTAURANT_ID to restaurantId
            )

            return DetailRestaurantFragment().apply {
                arguments = bundle
            }
        }
    }

    private val restaurantId by lazy {
        arguments?.getLong(RESTAURANT_ID, -1)
    }

    override val viewModel by viewModel<DetailRestaurantViewModel>()
    override fun getViewBinding(): FragmentDetailRestaurantBinding =
        FragmentDetailRestaurantBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun observeData() {
        viewModel.detailRestaurantStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is DetailRestaurantState.Uninitialized -> {
                    viewModel.getDetailRestaurant(restaurantId!!)
                    viewModel.getRestaurantImage(restaurantId!!)
                }

                is DetailRestaurantState.Loading -> {

                }

                is DetailRestaurantState.Success -> {
                    setDetailView(it.response.data)
                }

                is DetailRestaurantState.ImageSuccess -> {
                    setRestaurantImage(it.response.data.content)
                }

                is DetailRestaurantState.FavoriteSuccess -> {
                    val cnt = Integer.parseInt("${binding.tvFavoriteCnt.text}")
                    binding.tvFavoriteCnt.text = "${cnt+1}"
                }

                is DetailRestaurantState.UnFavoriteSuccess -> {
                    val cnt = Integer.parseInt("${binding.tvFavoriteCnt.text}")
                    binding.tvFavoriteCnt.text = "${cnt-1}"
                }

                is DetailRestaurantState.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initView() = with(binding) {
        toolbarRestaurantDetail.setNavigationOnClickListener {
            finish()
        }

        tbFavorite.setOnClickListener {
            if(tbFavorite.isChecked) {
                viewModel.favoriteRestaurant(restaurantId!!)
            } else {
                viewModel.unFavoriteRestaurant(restaurantId!!)
            }
        }
    }

    private fun setDetailView(data: DetailRestaurantData) = with(binding) {
        tvName.text = data.name
        tvFavoriteCnt.text = "${data.favoriteCount}"
        tbFavorite.isChecked = data.favorited
        tvLocation.text = data.address.roadName
        data.hashtags.forEach { hashTag ->
            flTag.addItem(hashTag)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setRestaurantImage(contents: List<RestaurantImageContent>) = with(binding) {
        if (contents.isEmpty()) {
            ivImage.setImageDrawable(requireContext().getDrawable(R.drawable.food1))
        } else {
            Glide.with(requireContext())
                .load(contents[0].imageUrl)
                .into(ivImage)
        }
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

    private fun dpToPx(dp: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
            .roundToInt()

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