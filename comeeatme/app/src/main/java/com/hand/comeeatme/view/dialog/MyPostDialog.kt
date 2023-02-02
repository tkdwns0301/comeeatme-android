package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.hand.comeeatme.databinding.DialogMyPostBinding

class MyPostDialog(
    context: Context,
    val modifyPost: () -> Unit,
    val deletePost: () -> Unit,
    val dynamicLink: () -> Unit,
) : Dialog(context) {
    private lateinit var binding: DialogMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogMyPostBinding.inflate(layoutInflater)
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
            modifyPost.invoke()
            dismiss()
        }

        llShare.setOnClickListener {
            dynamicLink.invoke()
            dismiss()
        }

        llDelete.setOnClickListener {
            deletePost.invoke()
            dismiss()
        }
    }
}