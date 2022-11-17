package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.databinding.LayoutMapItemBinding
import com.hand.comeeatme.view.main.map.MapDetailActivity

class MapAdapter(
    private val items: ArrayList<ArrayList<Int>>,
    private val context: Context,
) :
    RecyclerView.Adapter<MapAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            LayoutMapItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.container.setOnClickListener {
            val intent = Intent(context, MapDetailActivity::class.java)
            context.startActivity(intent)
        }
    }


    class PagerViewHolder(binding: LayoutMapItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container: ConstraintLayout = binding.clContainer
        val image1: ImageView = binding.icImage1.ivImage
        val image2: ImageView = binding.icImage2.ivImage
        val image3: ImageView = binding.icImage3.ivImage

    }
}

