package com.hand.comeeatme.util.widget.adapter
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.data.Thumbnail2
import com.hand.comeeatme.databinding.LayoutAlbumImageBinding

class AlbumAdapter(
    private val context: Context,
    private val images: List<Thumbnail2>,
    private val onClickImage: (image: Thumbnail2) -> Unit,
    private val onCheckedImage: (image: Thumbnail2) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutAlbumImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

        if(image.isChecked) {
            holder.checked.visibility = View.VISIBLE
        } else {
            holder.checked.visibility = View.GONE
        }

        //holder.image.setImageURI(image.uri)

        Glide.with(context)
            .load(image.uri)
            .into(holder.image)

        holder.image.setOnLongClickListener {
            Log.e("Image LongClick", "$position")
            return@setOnLongClickListener true
        }

        holder.image.setOnClickListener {
            onClickImage.invoke(image)
        }

        holder.checked.setOnClickListener {
            onCheckedImage.invoke(image)
        }



    }

    override fun getItemCount(): Int = images.size

    class ViewHolder(binding: LayoutAlbumImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivImage
        val checked = binding.viewChecked

    }



}