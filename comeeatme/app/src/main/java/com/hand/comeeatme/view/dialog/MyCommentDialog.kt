package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.hand.comeeatme.databinding.DialogCommentMyBinding

class MyCommentDialog(
    context: Context,
    val modifyComment: () -> Unit,
    val deleteComment: () -> Unit,
): Dialog(context) {
    private lateinit var binding: DialogCommentMyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogCommentMyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = context.resources.displayMetrics.widthPixels

        window!!.setLayout(
            (width - 200),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        setCanceledOnTouchOutside(true)
        setCancelable(true)

        llModify.setOnClickListener {
            modifyComment.invoke()
            dismiss()
        }

        llDelete.setOnClickListener {
            deleteComment.invoke()
            dismiss()
        }
    }
}