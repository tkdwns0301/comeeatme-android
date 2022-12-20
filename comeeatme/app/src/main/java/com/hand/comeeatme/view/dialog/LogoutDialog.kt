package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.hand.comeeatme.databinding.DialogLogoutBinding

class LogoutDialog(
    context: Context,
    val logout: () -> Unit,
) : Dialog(context) {
    private lateinit var binding: DialogLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = context.resources.displayMetrics.widthPixels

        window!!.setLayout(
            (width-200),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        setCancelable(true)
        setCanceledOnTouchOutside(true)

        tvCancel.setOnClickListener {
            dismiss()
        }

        tvOK.setOnClickListener {
            logout.invoke()
            dismiss()
        }
    }

}