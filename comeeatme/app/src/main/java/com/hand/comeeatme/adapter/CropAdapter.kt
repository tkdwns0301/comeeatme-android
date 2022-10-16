package com.hand.comeeatme.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.databinding.LayoutCropImageBinding
import java.io.File

class CropAdapter(
    private val items: ArrayList<String>,
    private val onClickImage: (image: String) -> Unit,
    private val visited: ArrayList<Boolean>
    )
    : RecyclerView.Adapter<CropAdapter.CropViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        val binding =
            LayoutCropImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CropViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CropAdapter.CropViewHolder, position: Int) {
        val image = items[position]


        holder.bind(image)

        if(visited[position]) {
            holder.checked.visibility = View.VISIBLE
        } else {
            holder.checked.visibility = View.GONE
        }

        holder.imageView.setOnClickListener {
            onClickImage.invoke(image)
        }

    }



    class CropViewHolder(binding: LayoutCropImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.ivImage
        val checked = binding.viewChecked
        fun bind(image: String) {
            imageView.setImageURI(Uri.fromFile(File(image)))
        }
    }


}

