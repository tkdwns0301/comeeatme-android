package com.hand.comeeatme.util.widget.adapter.rank

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.restaurant.RestaurantsRankContent
import com.hand.comeeatme.databinding.LayoutRankItemBinding
import com.hand.comeeatme.view.main.rank.restaurant.DetailRestaurantFragment
import kotlin.math.roundToInt

class RestaurantsRankAdapter(
    private val context: Context,
    private val items: List<RestaurantsRankContent>,
    private val favoriteRestaurant: (restaurantId: Long) -> Unit,
    private val unFavoriteRestaurant: (restaurantId: Long) -> Unit,
    private val depth1: String,
    private val depth2: String,
): RecyclerView.Adapter<RestaurantsRankAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutRankItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(context, item, position, depth1, depth2)

        holder.favorite.setOnClickListener {
            if(holder.favorite.isChecked) {
                favoriteRestaurant.invoke(item.id)
            } else {
                unFavoriteRestaurant.invoke(item.id)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(binding: LayoutRankItemBinding): RecyclerView.ViewHolder(binding.root) {
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

        private val ranking = binding.tvRanking
        private val image = binding.ivRestaurantImage
        private val restaurantName = binding.tvRestaurantName
        private val address = binding.tvAddress
        private val review = binding.tvReview
        private val hashtags = binding.flHashTag

        val favorite = binding.tbFavorite

        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun bind(context: Context, item: RestaurantsRankContent, position: Int, depth1: String, depth2: String) {
            ranking.text = "${position + 1}"

            if(item.imageUrls.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.default_image)
                    .into(image)
            } else {
                Glide.with(context)
                    .load(item.imageUrls[0])
                    .into(image)
            }

            restaurantName.text = item.name
            address.text = "$depth1 $depth2"
            review.text = "리뷰 ${item.postCount}개"
            favorite.isChecked = item.favorited

            item.hashtags.forEachIndexed {index,  hashtag ->
                if(index<2) {
                    hashtags.addItem(hashtag)
                }
            }

            itemView.setOnClickListener {
                val manager = (context as AppCompatActivity).supportFragmentManager
                val ft = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer, DetailRestaurantFragment.newInstance(item.id), DetailRestaurantFragment.TAG)
                ft.addToBackStack(DetailRestaurantFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }

        @SuppressLint( "InflateParams", "SetTextI18n")
        private fun FlexboxLayout.addItem(tag: String) {

            val view = LayoutInflater.from(context).inflate(R.layout.layout_hashtag_uncheck, null) as ConstraintLayout

            view.findViewById<TextView>(R.id.tv_HashTag).text = "#${hashTagEngToKor[tag]}"
            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )

            layoutParams.rightMargin = dpToPx(context,4)
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