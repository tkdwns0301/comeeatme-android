package com.hand.comeeatme.util.widget.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.post.RestaurantPostContent
import com.hand.comeeatme.databinding.LayoutRestaurantDetailItemBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment

class RestaurantPostsAdapter(
    private val context: Context,
    private val items: List<RestaurantPostContent>,
):RecyclerView.Adapter<RestaurantPostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutRestaurantDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(context, item)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(binding:LayoutRestaurantDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imageContainers = arrayListOf(
            binding.icImage1.cvImage, binding.icImage2.cvImage, binding.icImage3.cvImage
        )
        private val images = arrayListOf(
            binding.icImage1.ivImage, binding.icImage2.ivImage, binding.icImage3.ivImage
        )

        private val profile = binding.civProfile
        private val nickname = binding.tvNickname
        private val date = binding.tvDate
        private val content = binding.tvContent

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(context: Context, item: RestaurantPostContent) {
            item.imageUrls.forEachIndexed { index, imageUrl ->
                imageContainers[index].isVisible = true
                Glide.with(context)
                    .load(imageUrl)
                    .into(images[index])
            }

            if(item.member.imageUrl.isNullOrEmpty()) {
                Glide.with(context)
                    .load(R.drawable.default_profile)
                    .into(profile)
            } else {
                Glide.with(context)
                    .load(item.member.imageUrl)
                    .into(profile)
            }

            nickname.text = item.member.nickname
            date.text = item.createAt
            content.text = item.content

            itemView.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer, DetailPostFragment.newInstance(item.id), DetailPostFragment.TAG)
                ft.addToBackStack(DetailPostFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }
    }
}