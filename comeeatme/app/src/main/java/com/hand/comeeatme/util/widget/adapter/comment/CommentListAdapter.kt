package com.hand.comeeatme.util.widget.adapter.comment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.comment.CommentListContent
import com.hand.comeeatme.databinding.LayoutPostCommentBinding
import com.hand.comeeatme.view.dialog.MyCommentDialog
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class CommentListAdapter(
    private val items: List<CommentListContent>,
    private val context: Context,
    private val memberId: Long,
    val sendParentId: (parentId: Long, nickname: String) -> Unit,
    val modifyComment: (commentId: Long) -> Unit,
    val deleteComment: (commentId: Long) -> Unit,
) : RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item, context)
        holder.replyButton.setOnClickListener {
            sendParentId.invoke(item.id, item.member.nickname)
        }

        holder.option.setOnClickListener {
            if (memberId == item.member.id) {
                MyCommentDialog(context,
                    modifyComment = {
                        Log.e("modifyComment", "Click!!!TL@QKF")
                        modifyComment.invoke(item.id)
                    },
                    deleteComment = {
                        Log.e("deleteComment", "CLICK!!!TL@QKF")
                        deleteComment.invoke(item.id)
                    }
                ).show()
            } else {

            }
        }




    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(binding: LayoutPostCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val reply = binding.ivReply
        private val profile = binding.civProfile
        private val nickname = binding.tvNickName
        private val comment = binding.tvComment
        private val createdAt = binding.tvDate
        val replyButton = binding.tvReply
        val option = binding.ibOption

        @SuppressLint("UseCompatLoadingForDrawables", "SimpleDateFormat", "SetTextI18n")
        fun bind(item: CommentListContent, context: Context) {
            if(item.deleted) {
                Glide.with(context)
                    .load(R.drawable.default_profile)
                    .into(profile)

                nickname.text = "알수없음"
                comment.text = "사용자의 요청으로 삭제된 댓글입니다."

                if (item.parentId == null) {
                    reply.isGone = true
                    replyButton.isGone = true
                } else {
                    reply.isVisible = true
                    replyButton.isGone = true
                }

                option.isGone = true

            } else {
                if (item.parentId == null) {
                    reply.isGone = true
                    replyButton.isVisible = true
                } else {
                    reply.isVisible = true
                    replyButton.isGone = true
                }

                if (item.member.imageUrl.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(R.drawable.default_profile)
                        .into(profile)
                } else {
                    Glide.with(context)
                        .load(item.member.imageUrl)
                        .into(profile)
                }

                nickname.text = item.member.nickname
                comment.text = item.content

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
}