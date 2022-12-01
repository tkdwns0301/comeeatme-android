package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent
import com.hand.comeeatme.databinding.LayoutNewpostSearchRestaurantBinding

class SearchNewPostRestaurantAdapter(
    private val context: Context,
    private val items: List<SimpleRestaurantContent>,
    private val isDetailRestaurant: Boolean,
    val onClickItem: (restaurant: SimpleRestaurantContent) -> Unit,
) : RecyclerView.Adapter<SearchNewPostRestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchNewPostRestaurantAdapter.ViewHolder {
        val binding =
            LayoutNewpostSearchRestaurantBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)

        return SearchNewPostRestaurantAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchNewPostRestaurantAdapter.ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item, onClickItem, isDetailRestaurant)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutNewpostSearchRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val name = binding.tvName
        private val address = binding.tvAddress

        fun bind(
            item: SimpleRestaurantContent,
            onClickItem: (restaurant: SimpleRestaurantContent) -> Unit,
            isDetailRestaurant: Boolean,
        ) {
            name.text = item.name
            address.text = item.addressName

            itemView.setOnClickListener {
                // TODO 식당 상세페이지
                if (isDetailRestaurant) {

                } else {
                    onClickItem.invoke(item)
                }
            }
        }
    }
}