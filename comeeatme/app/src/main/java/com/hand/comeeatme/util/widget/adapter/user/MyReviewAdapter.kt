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
import com.hand.comeeatme.databinding.LayoutUserMyreviewBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment

class MyReviewAdapter(
    private val items: List<Content>,
    private val context: Context,
) : RecyclerView.Adapter<MyReviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutUserMyreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(context, item)
    }

    class ViewHolder(binding: LayoutUserMyreviewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.ivImage
        private val content = binding.tvContent
        private val date = binding.tvDate
        private val view = binding.tvView
        private val commentCount = binding.tvCommentCnt

        @SuppressLint("SetTextI18n")
        fun bind(context: Context, item: Content) {
            Glide.with(context)
                .load(item.imageUrls[0])
                .into(image)

            content.text = item.content
            date.text = item.createdAt
            commentCount.text = "${item.commentCount}"

            itemView.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer,
                    DetailPostFragment.newInstance(item.id),
                    DetailPostFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }
    }
}