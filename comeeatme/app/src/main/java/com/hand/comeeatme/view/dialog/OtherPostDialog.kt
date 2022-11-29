package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.hand.comeeatme.databinding.DialogOtherPostBinding

class OtherPostDialog(
    context: Context
): Dialog(context) {
    private lateinit var binding: DialogOtherPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogOtherPostBinding.inflate(layoutInflater)
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

        llBookmark.setOnClickListener {
            // TODO 북마크
            dismiss()
        }

        llShare.setOnClickListener {
            // TODO 공유하기
            dismiss()
        }

        llReport.setOnClickListener {
            // TODO 신고하기
            dismiss()
        }
    }

}