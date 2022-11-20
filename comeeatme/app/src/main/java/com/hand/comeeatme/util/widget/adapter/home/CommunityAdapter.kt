package com.hand.comeeatme.util.widget.adapter.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.network.LikeService
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.LayoutHomeItemBinding
import com.hand.comeeatme.util.widget.adapter.ViewPagerAdapter
import com.hand.comeeatme.view.main.home.post.DetailPostFragment

class CommunityAdapter(
    private val items: List<Content>,
    private val context: Context,
    val likePost: (postId: Long) -> Unit,
    val unLikePost: (postId: Long) -> Unit,
    val bookmarkPost: (postId: Long) -> Unit,
    val unBookmarkPost: (postId: Long) -> Unit,
) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {
    private lateinit var likeService: LikeService


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.viewPager.adapter = ViewPagerAdapter(item.id, item.imageUrls, context, false)
        holder.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        holder.container.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()
            Log.e("Adapter: postId, position", "${item.id}, $position")

            ft.add(R.id.fg_MainContainer, DetailPostFragment.newInstance(item.id), "fm_Post")
            ft.commitAllowingStateLoss()
        }

        when (item.imageUrls.size) {
            1 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image1_24)
            }
            2 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image2_24)
            }
            3 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image3_24)
            }
            4 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image4_24)
            }
            5 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image5_24)
            }
            6 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image6_24)
            }
            7 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image7_24)
            }
            8 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image8_24)
            }
            9 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image9_24)
            }
            10 -> {
                holder.imageCount.setImageResource(R.drawable.ic_image10_24)
            }
        }

        holder.location.text = "${item.restaurant.name}"
        holder.name.text = "${item.member.nickname}"

        if (item.member.imageUrl.isNullOrEmpty()) {
            holder.profile.setImageDrawable(context.getDrawable(R.drawable.food1))
        } else {
            Glide.with(context)
                .load(item.member.imageUrl)
                .into(holder.profile)
        }

        holder.content.text = "${item.content}"
        holder.commentCount.text = "(${item.commentCount})"
        holder.likeCount.text = "(${item.likeCount})"

        holder.like.isChecked = item.liked
        holder.bookmark.isChecked = item.bookmarked


        holder.like.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                likePost.invoke(item.id)
            } else {
                unLikePost.invoke(item.id)
            }
        }

        holder.bookmark.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bookmarkPost.invoke(item.id)
            } else {
                unBookmarkPost.invoke(item.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutHomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container = binding.clHomeItem
        val viewPager = binding.vpPhotos
        val imageCount: ImageView = binding.ivNumber
        val location: TextView = binding.tvLocation
        val locationContainer = binding.clLocation
        val name = binding.tvNickName
        val profile = binding.cvProfile
        val content = binding.tvContent
        val commentCount = binding.tvComment
        val likeCount = binding.tvLike
        val like = binding.tbLike
        val bookmark = binding.tbBookmark
    }
}