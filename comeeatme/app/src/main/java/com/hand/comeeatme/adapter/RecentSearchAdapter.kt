package com.hand.comeeatme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.LayoutRecentsearchItemBinding

class RecentSearchAdapter(
    private val items: ArrayList<String>,
    private val onClickDeleteIcon: (position: Int) -> Unit
) : RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutRecentsearchItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.profile.setImageResource(R.drawable.food1)

        holder.name.text = items[position]

        holder.delete.setOnClickListener {
            onClickDeleteIcon.invoke(position)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(binding: LayoutRecentsearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val profile = binding.cvProfile
        val name = binding.tvName
        val delete = binding.ibDelete

    }
}
