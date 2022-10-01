package com.hand.comeeatme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hand.comeeatme.databinding.LayoutHomeItemBinding

class CommunityAdapter(
    private val items: ArrayList<ArrayList<Int>>
    ) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapter.ViewHolder {
        val binding =
            LayoutHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewPager.adapter = ViewPagerAdapter(items[position])
        holder.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutHomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewPager = binding.vpImages
    }
}