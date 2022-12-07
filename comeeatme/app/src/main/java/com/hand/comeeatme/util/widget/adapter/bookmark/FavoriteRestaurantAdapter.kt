package com.hand.comeeatme.util.widget.adapter.bookmark

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.favorite.FavoritePostContent
import com.hand.comeeatme.databinding.LayoutBookmarkFavoriteItemBinding
import com.hand.comeeatme.view.main.rank.restaurant.DetailRestaurantFragment
import kotlin.math.roundToInt

class FavoriteRestaurantAdapter(
    private val context: Context,
    private val items: List<FavoritePostContent>,
    val favoriteRestaurant : (restaurantId: Long) -> Unit,
    val unFavoriteRestaurant: (restaurantId: Long) -> Unit,

) : RecyclerView.Adapter<FavoriteRestaurantAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutBookmarkFavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(context, item)

        holder.favorited.setOnClickListener {
            if(holder.favorited.isChecked) {
                favoriteRestaurant.invoke(item.id)
            } else {
                unFavoriteRestaurant.invoke(item.id)
            }
        }
    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(binding: LayoutBookmarkFavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imageContainers = arrayListOf(
            binding.icImage1.cvImage, binding.icImage2.cvImage, binding.icImage3.cvImage
        )
        private val images = arrayListOf(
            binding.icImage1.ivImage, binding.icImage2.ivImage, binding.icImage3.ivImage
        )
        private val restaurantName = binding.tvName
        val favorited = binding.tbFavorite
        private val hashTags = binding.flTag

        fun bind(context: Context, item: FavoritePostContent) {
            restaurantName.text = item.name
            favorited.isChecked = item.favorited

            itemView.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer, DetailRestaurantFragment.newInstance(item.id), DetailRestaurantFragment.TAG)
                ft.addToBackStack(DetailRestaurantFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }

        @SuppressLint( "InflateParams", "SetTextI18n")
        private fun FlexboxLayout.addItem(tag: String, context: Context) {

            val chip = LayoutInflater.from(context).inflate(R.layout.layout_chip_custom, null) as Chip

            chip.apply {
                text = "#$tag"
                textSize = 13f
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

            layoutParams.rightMargin = dpToPx(10, context)
            addView(chip, childCount, layoutParams)
        }

        private fun dpToPx(dp: Int, context: Context): Int =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics
            )
                .roundToInt()
    }



}