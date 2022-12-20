package com.hand.comeeatme.util.widget.adapter.user

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.LayoutUserLikedpostBinding
import com.hand.comeeatme.databinding.LayoutUserMyreviewBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class LikedPostAdapter(
    private val context: Context,
    private val items: List<Content>,
) : RecyclerView.Adapter<LikedPostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutUserLikedpostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(context, item)
    }

    class ViewHolder(binding: LayoutUserLikedpostBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.ivImage
        private val content = binding.tvContent
        private val createdAt = binding.tvDate
        private val view = binding.tvView
        private val commentCount = binding.tvCommentCnt

        @SuppressLint("SetTextI18n")
        fun bind(context: Context, item: Content) {
            Glide.with(context)
                .load(item.imageUrls[0])
                .into(image)

            content.text = item.content

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

            commentCount.text = "${item.commentCount}"

            itemView.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer,
                    DetailPostFragment.newInstance(item.id),
                    DetailPostFragment.TAG)
                ft.addToBackStack(DetailPostFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }
    }
}