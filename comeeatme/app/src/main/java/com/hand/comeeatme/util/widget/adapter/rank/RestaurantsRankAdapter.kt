package com.hand.comeeatme.util.widget.adapter.rank

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.restaurant.RestaurantsRankContent
import com.hand.comeeatme.databinding.LayoutRankItemBinding
import com.hand.comeeatme.view.main.rank.restaurant.DetailRestaurantFragment

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
        private val ranking = binding.tvRanking
        private val image = binding.ivRestaurantImage
        private val restaurantName = binding.tvRestaurantName
        private val address = binding.tvAddress
        private val review = binding.tvReview
        val favorite = binding.tbFavorite

        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun bind(context: Context, item: RestaurantsRankContent, position: Int, depth1: String, depth2: String) {
            ranking.text = "${position + 1}"

            if(item.imageUrls.isEmpty()) {
                image.setImageDrawable(context.getDrawable(R.drawable.food1))
            } else {
                Glide.with(context)
                    .load(item.imageUrls[0])
                    .into(image)
            }

            restaurantName.text = item.name
            address.text = "$depth1 $depth2"
            review.text = "리뷰 ${item.postCount}개"

            favorite.isChecked = item.favorited

            itemView.setOnClickListener {
                val manager = (context as AppCompatActivity).supportFragmentManager
                val ft = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer, DetailRestaurantFragment.newInstance(item.id), DetailRestaurantFragment.TAG)
                ft.addToBackStack(DetailRestaurantFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }


    }
}