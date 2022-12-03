package com.hand.comeeatme.util.widget.adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.data.Thumbnail
import com.hand.comeeatme.databinding.LayoutAlbumImageBinding

class AlbumAdapter(
    private val images: ArrayList<Thumbnail>,
    private val onClickImage: (image: Thumbnail) -> Unit,
    private val onCheckedImage: (image: Thumbnail) -> Unit
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

        holder.image.setImageBitmap(image.thumbnail)
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