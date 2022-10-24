package com.hand.comeeatme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.databinding.LayoutMapBottomsheetItemBinding

class BottomSheetAdapter(
    private val items: ArrayList<String>,
    private val context: Context
    ) : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetAdapter.ViewHolder {
        val binding =
            LayoutMapBottomsheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = "${items[position]}"

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutMapBottomsheetItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvName
    }
}