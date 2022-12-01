package com.hand.comeeatme.util.widget.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.home.NicknameContent
import com.hand.comeeatme.databinding.LayoutSearchUserBinding

class SearchUserAdapter(
    private val items: List<NicknameContent>,
    private val context: Context,
): RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(context, item)
    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(binding: LayoutSearchUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val profile = binding.civProfile
        val nickname = binding.tvNickName

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(context: Context, item: NicknameContent) {
            if(item.imageUrl.isNullOrEmpty()) {
                profile.setImageDrawable(context.getDrawable(R.drawable.food1))
            } else {
                Glide.with(context)
                    .load(item.imageUrl)
                    .into(profile)
            }

            nickname.text = item.nickname

            itemView.setOnClickListener {
                // TODO 사용자 상세페이지, memberId 넘겨주기
            }
        }
    }


}