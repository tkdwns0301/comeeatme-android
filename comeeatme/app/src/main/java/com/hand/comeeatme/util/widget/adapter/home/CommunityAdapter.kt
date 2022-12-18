package com.hand.comeeatme.util.widget.adapter.home

import android.annotation.SuppressLint
import android.content.Context
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
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.LayoutHomeItemBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment
import com.hand.comeeatme.view.main.user.other.OtherPageFragment
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class CommunityAdapter(
    private val items: List<Content>,
    private val context: Context,
    val likePost: (postId: Long) -> Unit,
    val unLikePost: (postId: Long) -> Unit,
    val bookmarkPost: (postId: Long) -> Unit,
    val unBookmarkPost: (postId: Long) -> Unit,
) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(context, item)

        holder.like.setOnClickListener {
            if (holder.like.isChecked) {
                likePost.invoke(item.id)
            } else {
                unLikePost.invoke(item.id)
            }
        }

        holder.bookmark.setOnClickListener {
            if (holder.bookmark.isChecked) {
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

        private val viewPager = binding.vpPhotos
        private val imageCount: ImageView = binding.ivNumber
        private val location: TextView = binding.tvLocation

        private val nickname = binding.tvNickName
        private val profile = binding.cvProfile
        private val content = binding.tvContent
        private val commentCount = binding.tvComment
        private val likeCount = binding.tvLike
        private val createdAt = binding.tvDate

        val like = binding.tbLike
        val bookmark = binding.tbBookmark

        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "SimpleDateFormat")
        fun bind(context: Context, item: Content) {
            viewPager.adapter = ViewPagerAdapter(item.id, item.imageUrls, context, false)
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            when (item.imageUrls.size) {
                1 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image1_24)
                        .into(imageCount)
                }
                2 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image2_24)
                        .into(imageCount)
                }
                3 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image3_24)
                        .into(imageCount)
                }
                4 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image4_24)
                        .into(imageCount)
                }
                5 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image5_24)
                        .into(imageCount)
                }
                6 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image6_24)
                        .into(imageCount)
                }
                7 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image7_24)
                        .into(imageCount)
                }
                8 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image8_24)
                        .into(imageCount)
                }
                9 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image9_24)
                        .into(imageCount)
                }
                10 -> {
                    Glide.with(context)
                        .load(R.drawable.ic_image10_24)
                        .into(imageCount)
                }
            }

            location.text = item.restaurant.name
            nickname.text = item.member.nickname

            if (item.member.imageUrl.isNullOrEmpty()) {
                Glide.with(context)
                    .load(R.drawable.default_profile)
                    .into(profile)
            } else {
                Glide.with(context)
                    .load(item.member.imageUrl)
                    .into(profile)
            }

            content.text = item.content
            commentCount.text = "(${item.commentCount})"
            likeCount.text = "(${item.likeCount})"

            like.isChecked = item.liked
            bookmark.isChecked = item.bookmarked


            val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            val createdTime = sdf.parse(item.createdAt)
            val createdMillis = createdTime.time

            val currMillis = System.currentTimeMillis()

            var diff = (currMillis - createdMillis)

            when {
                diff < 60000 -> {
                    createdAt.text = "방금 전"
                }
                diff < 3600000 -> {
                    createdAt.text = "${TimeUnit.MILLISECONDS.toMinutes(diff)}분 전"
                }
                diff < 86400000 -> {
                    createdAt.text = "${TimeUnit.MILLISECONDS.toHours(diff)}시간 전"
                }
                diff < 604800000 -> {
                    createdAt.text = "${TimeUnit.MILLISECONDS.toDays(diff)}일 전"
                }
                diff < 2419200000 -> {
                    createdAt.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 7}주 전"
                }
                diff < 31556952000 -> {
                    createdAt.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 30}개월 전"
                }
                else -> {
                    createdAt.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 365}년 전"
                }
            }

            itemView.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()


                ft.add(R.id.fg_MainContainer,
                    DetailPostFragment.newInstance(item.id),
                    DetailPostFragment.TAG)
                ft.addToBackStack(DetailPostFragment.TAG).commitAllowingStateLoss()

            }

            nickname.setOnClickListener {
                toOtherFragment(context, item)
            }

            profile.setOnClickListener {
                toOtherFragment(context, item)
            }

        }

        private fun toOtherFragment(context: Context, item: Content) {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer,
                OtherPageFragment.newInstance(item.member.id),
                OtherPageFragment.TAG)
            ft.addToBackStack(OtherPageFragment.TAG).commitAllowingStateLoss()
        }
    }
}