package com.hand.comeeatme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.databinding.LayoutHomeImageBinding

class ViewPagerAdapter(list: ArrayList<Int>): RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PagerViewHolder{
        val binding = LayoutHomeImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.images.setImageResource(item[position])
        holder.number.text = (position+1).toString()
    }


    class PagerViewHolder(binding: LayoutHomeImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val images: ImageView = binding.ivHomeItem
        val number: TextView = binding.tvNumber
    }
}

