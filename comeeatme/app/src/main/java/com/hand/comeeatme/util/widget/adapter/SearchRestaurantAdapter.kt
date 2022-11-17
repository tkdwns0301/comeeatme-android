package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent
import com.hand.comeeatme.databinding.LayoutNewpostSearchRestaurantBinding

class SearchRestaurantAdapter(
    private val context: Context,
    private val items: List<SimpleRestaurantContent>,
    private val onClickItem: (restaurant: SimpleRestaurantContent) -> Unit,
) : RecyclerView.Adapter<SearchRestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchRestaurantAdapter.ViewHolder {
        val binding =
            LayoutNewpostSearchRestaurantBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)

        return SearchRestaurantAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchRestaurantAdapter.ViewHolder, position: Int) {
        holder.name.text = items[position].name
        holder.address.text = items[position].addressName

        holder.container.setOnClickListener {
            onClickItem.invoke(items[position])
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutNewpostSearchRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val container = binding.container
        val name = binding.tvName
        val address = binding.tvAddress
    }
}