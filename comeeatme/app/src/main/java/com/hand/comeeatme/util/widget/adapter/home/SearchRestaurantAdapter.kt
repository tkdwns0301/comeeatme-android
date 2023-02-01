package com.hand.comeeatme.util.widget.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent
import com.hand.comeeatme.databinding.LayoutSearchRestaurantBinding
import com.hand.comeeatme.view.main.rank.restaurant.DetailRestaurantFragment

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

        holder.bind(context, item)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutSearchRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.tvRestaurantName
        private val address = binding.tvAddress

        fun bind(context: Context, item: SimpleRestaurantContent) {
            name.text = item.name
            address.text = item.addressName

            itemView.setOnClickListener {
                // TODO 식당 상세페이지
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer, DetailRestaurantFragment.newInstance(item.id), DetailRestaurantFragment.TAG)
                ft.addToBackStack(DetailRestaurantFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }
    }
}