package com.hand.comeeatme.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.databinding.LayoutNewpostImageBinding

class NewPostImagesAdapter(
    private val images: ArrayList<Uri>,
    private val onClickDeleteIcon: (image: Uri) -> Unit
) : RecyclerView.Adapter<NewPostImagesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutNewpostImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.e("NewPostAdapter", "true!!!!")
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

        holder.image.setImageURI(image)

        holder.cancel.setOnClickListener {
            Log.e("Cancel", "Clicked!!!")

            onClickDeleteIcon.invoke(image)
        }
    }

    override fun getItemCount(): Int = images.size


    class ViewHolder(binding: LayoutNewpostImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivImage
        val cancel = binding.ibCancelImage


    }



}