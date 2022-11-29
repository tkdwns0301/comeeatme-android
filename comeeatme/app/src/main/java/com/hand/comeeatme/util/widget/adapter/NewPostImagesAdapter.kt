package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.databinding.LayoutNewpostImageBinding
import java.io.File

class NewPostPhotosAdapter(
    private val context: Context,
    private val photos: ArrayList<String>,
    private val isModify: Boolean,
) : RecyclerView.Adapter<NewPostPhotosAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutNewpostImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoPath = photos[position]

        if(isModify) {
            Glide.with(context)
                .load(photoPath)
                .into(holder.photoLayout)
        }
        holder.photoLayout.setImageURI(Uri.fromFile(File(photoPath)))
    }

    override fun getItemCount(): Int = photos.size


    class ViewHolder(binding: LayoutNewpostImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val photoLayout = binding.ivImage
    }



}