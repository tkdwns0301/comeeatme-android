package com.hand.comeeatme.util.widget.adapter.bookmark

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
import com.hand.comeeatme.data.response.bookmark.BookmarkPostContent
import com.hand.comeeatme.databinding.LayoutBookmarkPostItemBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment

class BookmarkPostAdapter(
    private val context: Context,
    private val items: List<BookmarkPostContent>,
    val bookmarkPost: (postId: Long) -> Unit,
    val unBookmarkPost: (postId: Long) -> Unit,
) : RecyclerView.Adapter<BookmarkPostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutBookmarkPostItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(context, item)
    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(binding: LayoutBookmarkPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image1 = binding.icImage1.ivImage
        private val image2 = binding.icImage2.ivImage
        private val image3 = binding.icImage3.ivImage
        private val profile = binding.clImage
        private val nickname = binding.tvName
        private val bookmark = binding.tbBookmark
        private val content = binding.tvContent

        fun bind(context: Context, item: BookmarkPostContent) {
            if (item.imageUrls[0].isNullOrEmpty()) {
                image1.setImageDrawable(context.getDrawable(R.drawable.food1))
            } else {
                Glide.with(context)
                    .load(item.imageUrls[0])
                    .into(image1)
            }

            if (item.imageUrls[1].isNullOrEmpty()) {
                image2.setImageDrawable(context.getDrawable(R.drawable.food1))
            } else {
                Glide.with(context)
                    .load(item.imageUrls[1])
                    .into(image2)
            }

            if (item.imageUrls[2].isNullOrEmpty()) {
                image3.setImageDrawable(context.getDrawable(R.drawable.food1))
            } else {
                Glide.with(context)
                    .load(item.imageUrls[2])
                    .into(image3)
            }

            if (item.member.imageUrl.isNullOrEmpty()) {
                profile.setImageDrawable(context.getDrawable(R.drawable.food1))
            } else {
                Glide.with(context)
                    .load(item.member.imageUrl)
                    .into(profile)
            }

            nickname.text = item.member.nickname
            bookmark.isChecked = item.bookmarked
            content.text = item.content

            itemView.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer, DetailPostFragment.newInstance(item.id), DetailPostFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }
    }


}