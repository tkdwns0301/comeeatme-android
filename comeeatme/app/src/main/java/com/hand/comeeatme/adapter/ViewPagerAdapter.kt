package com.hand.comeeatme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.LayoutHomeImageBinding

class ViewPagerAdapter(list: ArrayList<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            LayoutHomeImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.images.setImageResource(item[position])

        when (itemCount) {
            1 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image1)
            }
            2 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image2)
            }
            3 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image3)
            }
            4 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image4)
            }
            5 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image5)
            }
            6 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image6)
            }
            7 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image7)
            }
            8 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image8)
            }
            9 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image9)
            }
            10 -> {
                holder.imageSize.setImageResource(R.drawable.ic_image10)
            }
        }

        holder.location.text = "맛있는 피자집ㅋ"
    }


    class PagerViewHolder(binding: LayoutHomeImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val images: ImageView = binding.ivHomeItem
        val imageSize: ImageView = binding.ivNumber
        val location: TextView = binding.tvLocation
    }
}

