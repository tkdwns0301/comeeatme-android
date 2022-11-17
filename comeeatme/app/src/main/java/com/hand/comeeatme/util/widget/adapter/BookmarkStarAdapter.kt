package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.databinding.LayoutBookmarkStarItemBinding

class BookmarkStarAdapter(
    private val context: Context,
    private val items: ArrayList<Int>
) : RecyclerView.Adapter<BookmarkStarAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutBookmarkStarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(binding: LayoutBookmarkStarItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }



}