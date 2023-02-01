package com.hand.comeeatme.util.widget.adapter.bookmark

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.favorite.FavoritePostContent
import com.hand.comeeatme.databinding.LayoutBookmarkFavoriteItemBinding
import com.hand.comeeatme.view.main.rank.restaurant.DetailRestaurantFragment
import kotlin.math.roundToInt

class FavoriteRestaurantAdapter(
    private val context: Context,
    private val items: List<FavoritePostContent>,
    val favoriteRestaurant: (restaurantId: Long) -> Unit,
    val unFavoriteRestaurant: (restaurantId: Long) -> Unit,

    ) : RecyclerView.Adapter<FavoriteRestaurantAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutBookmarkFavoriteItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(context, item)

        holder.favorited.setOnClickListener {
            if (holder.favorited.isChecked) {
                favoriteRestaurant.invoke(item.id)
            } else {
                unFavoriteRestaurant.invoke(item.id)
            }
        }
    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(binding: LayoutBookmarkFavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val hashTagEngToKor = hashMapOf<String, String>(
            "MOODY" to "감성있는",
            "EATING_ALON" to "혼밥",
            "GROUP_MEETING" to "단체모임",
            "DATE" to "데이트",
            "SPECIAL_DAY" to "특별한 날",
            "FRESH_INGREDIENT" to "신선한 재료",
            "SIGNATURE_MENU" to "시그니쳐 메뉴",
            "COST_EFFECTIVENESS" to "가성비",
            "LUXURIOUSNESS" to "고급스러운",
            "STRONG_TASTE" to "자극적인",
            "KINDNESS" to "친절",
            "CLEANLINESS" to "청결",
            "PARKING" to "주차장",
            "PET" to "반려동물 동반",
            "CHILD" to "아이 동반",
            "AROUND_CLOCK" to "24시간"
        )

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

            if(!item.imageUrls.isNullOrEmpty()) {
                item.imageUrls.forEachIndexed { index, imageUrl ->
                    if (index < 3) {
                        imageContainers[index].isVisible = true
                        Glide.with(context)
                            .load(imageUrl)
                            .into(images[index])
                    }
                }
            }

            item.hashtags.forEach { hashTag ->
                hashTags.addItem(hashTag)
            }

            itemView.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer,
                    DetailRestaurantFragment.newInstance(item.id),
                    DetailRestaurantFragment.TAG)
                ft.addToBackStack(DetailRestaurantFragment.TAG)
                ft.commitAllowingStateLoss()
            }


        }

        @SuppressLint("InflateParams", "SetTextI18n")
        private fun FlexboxLayout.addItem(tag: String) {

            val view = LayoutInflater.from(context)
                .inflate(R.layout.layout_hashtag_uncheck, null) as ConstraintLayout

            view.findViewById<TextView>(R.id.tv_HashTag).text = "#${hashTagEngToKor[tag]}"
            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )

            layoutParams.rightMargin = dpToPx(context, 4)
            addView(view, childCount, layoutParams)
        }

        private fun dpToPx(context: Context, dp: Int): Int =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics
            )
                .roundToInt()
    }


}