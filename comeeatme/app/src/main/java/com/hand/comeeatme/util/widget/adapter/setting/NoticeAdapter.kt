package com.hand.comeeatme.util.widget.adapter.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.notice.NoticesContent
import com.hand.comeeatme.databinding.LayoutNoticeItemBinding
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class NoticeAdapter(
    private val context: Context,
    private val items: List<NoticesContent>,
) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutNoticeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(context, item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutNoticeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val type = binding.clType
        private val title = binding.tvTitle
        private val content = binding.tvContent
        private val createdAt = binding.tvCreatedAt

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(context: Context, item: NoticesContent) {
            if(item.type == "NOTICE") {
                title.text = "공지사항"
                type.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))
            } else if(item.type == "EVENT") {
                title.text = "이벤트 안내"
                type.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.basic))
            }

            content.text = item.content

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            val createdTime = sdf.parse(item.createdAt)
            val createdMillis = createdTime.time

            val currMillis = System.currentTimeMillis()

            var diff = (currMillis - createdMillis)

            when {
                diff < 60000 -> {
                    createdAt.text = "방금 전"
                }
                diff < 3600000 -> {
                    createdAt.text = "${TimeUnit.MILLISECONDS.toMinutes(diff)}분 전"
                }
                diff < 86400000 -> {
                    createdAt.text = "${TimeUnit.MILLISECONDS.toHours(diff)}시간 전"
                }
                diff < 604800000 -> {
                    createdAt.text = "${TimeUnit.MILLISECONDS.toDays(diff)}일 전"
                }
                diff < 2419200000 -> {
                    createdAt.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 7}주 전"
                }
                diff < 31556952000 -> {
                    createdAt.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 30}개월 전"
                }
                else -> {
                    createdAt.text = "${(TimeUnit.MILLISECONDS.toDays(diff)) / 365}년 전"
                }
            }
        }
    }
}