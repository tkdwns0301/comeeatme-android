package com.hand.comeeatme.util.widget.adapter.bookmark

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.data.response.favorite.FavoritePostContent
import com.hand.comeeatme.databinding.LayoutBookmarkFavoriteItemBinding

class FavoritePostAdapter(
    private val context: Context,
    private val items: List<FavoritePostContent>
) : RecyclerView.Adapter<FavoritePostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutBookmarkFavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]


    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(binding: LayoutBookmarkFavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(context: Context, item: FavoritePostContent) {

        }
    }



}