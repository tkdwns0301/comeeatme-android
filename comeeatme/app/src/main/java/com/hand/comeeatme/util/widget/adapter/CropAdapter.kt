package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.databinding.LayoutCropImageBinding
import java.io.File

class CropAdapter(
    private val context: Context,
    private val items: ArrayList<String>,
    private val onClickImage: (photoPath: String) -> Unit,
    private val visited: ArrayList<Boolean>,
    )
    : RecyclerView.Adapter<CropAdapter.CropViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        val binding =
            LayoutCropImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CropViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
        val photo = items[position]

        holder.bind(photo, context)

        if(visited[position]) {
            holder.checked.visibility = View.VISIBLE
        } else {
            holder.checked.visibility = View.GONE
        }

        holder.imageView.setOnClickListener {
            onClickImage.invoke(photo)
        }

    }



    class CropViewHolder(binding: LayoutCropImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.ivImage
        val checked = binding.viewChecked

        fun bind(photoPath: String, context: Context) {
            Glide.with(context)
                .load(Uri.fromFile(File(photoPath)))
                .into(imageView)
        }
    }


}

