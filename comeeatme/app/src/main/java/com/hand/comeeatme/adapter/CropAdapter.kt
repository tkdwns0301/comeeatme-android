package com.hand.comeeatme.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.data.CropImage
import com.hand.comeeatme.databinding.LayoutCropBinding

class CropAdapter(list: ArrayList<CropImage>) :
    RecyclerView.Adapter<CropAdapter.CropViewHolder>() {
    var item = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        val binding =
            LayoutCropBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CropViewHolder(binding)
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: CropAdapter.CropViewHolder, position: Int) {
        val image = item[position]


        holder.bind(image.image)





    }

    class CropViewHolder(binding: LayoutCropBinding) : RecyclerView.ViewHolder(binding.root) {
        val cropLayout = binding.clCropImage

        fun bind(image: Uri) {
            cropLayout.setUri(image)
        }
    }


}

