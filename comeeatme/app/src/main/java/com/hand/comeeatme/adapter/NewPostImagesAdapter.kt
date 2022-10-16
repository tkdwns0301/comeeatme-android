package com.hand.comeeatme.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.databinding.LayoutNewpostImageBinding

class NewPostImagesAdapter(
    private val images: ArrayList<Uri>,
    private val onClickDeleteIcon: (position: Int) -> Unit
) : RecyclerView.Adapter<NewPostImagesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutNewpostImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

        holder.image.setImageURI(image)


    }

    override fun getItemCount(): Int = images.size


    class ViewHolder(binding: LayoutNewpostImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivImage
    }



}