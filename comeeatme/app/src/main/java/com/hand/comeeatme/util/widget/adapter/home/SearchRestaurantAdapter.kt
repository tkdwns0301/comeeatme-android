package com.hand.comeeatme.util.widget.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent
import com.hand.comeeatme.databinding.LayoutSearchRestaurantBinding

class SearchRestaurantAdapter(
    private val context: Context,
    private val items: List<SimpleRestaurantContent>,
) : RecyclerView.Adapter<SearchRestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchRestaurantAdapter.ViewHolder {
        val binding =
            LayoutSearchRestaurantBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)

        return SearchRestaurantAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchRestaurantAdapter.ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutSearchRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.tvRestaurantName
        private val address = binding.tvAddress

        fun bind(item: SimpleRestaurantContent) {
            name.text = item.name
            address.text = item.addressName

            itemView.setOnClickListener {
                // TODO 식당 상세페이지
            }
        }
    }
}