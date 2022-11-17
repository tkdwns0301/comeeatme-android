package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.databinding.LayoutBookmarkPostItemBinding

class BookmarkPostAdapter(
    private val context: Context,
    private val items: ArrayList<Int>
) : RecyclerView.Adapter<BookmarkPostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutBookmarkPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(binding: LayoutBookmarkPostItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }



}