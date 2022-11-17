package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.LayoutHomeImageBinding
import com.hand.comeeatme.view.main.home.post.PostFragment

class ViewPagerAdapter(
    private val items: ArrayList<String>,
    private val context: Context,
) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            LayoutHomeImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val item = items[position]

        if(item.isEmpty()) {
            holder.images.setImageDrawable(context.getDrawable(R.drawable.food1))
        } else {
            Glide.with(context)
                .load(item)
                .into(holder.images)
        }


        holder.images.setOnClickListener {
            val manager = (context as AppCompatActivity).supportFragmentManager
            val ft = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, PostFragment(), "fm_Post")
            ft.commitAllowingStateLoss()
        }
    }


    class PagerViewHolder(binding: LayoutHomeImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val images: ImageView = binding.ivHomeItem
    }
}

