package com.hand.comeeatme.util.widget.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.LayoutHomeImageBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment

class ViewPagerAdapter(
    private val postId: Long,
    private val items: List<String>,
    private val context: Context,
    private val isDetail: Boolean,
) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            LayoutHomeImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val item = items[position]

        if (item.isEmpty()) {
            Glide.with(context)
                .load(R.drawable.default_image)
                .into(holder.images)
        } else {
            Glide.with(context)
                .load(item)
                .into(holder.images)
        }

        if (!isDetail) {
            holder.images.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer, DetailPostFragment.newInstance(postId), DetailPostFragment.TAG)
                ft.addToBackStack(DetailPostFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }

    }


    class PagerViewHolder(binding: LayoutHomeImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val images: ImageView = binding.ivHomeItem
    }
}

