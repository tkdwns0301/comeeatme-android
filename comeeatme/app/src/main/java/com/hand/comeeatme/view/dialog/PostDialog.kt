package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.LinearLayout
import com.hand.comeeatme.R

class PostDialog(private val context: Context) {
    private val dialog = Dialog(context)


    fun showDialog() {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_post)

        val width = context.resources.displayMetrics.widthPixels

        dialog.window!!.setLayout(
            (width - 200),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.findViewById<LinearLayout>(R.id.ll_Bookmark).setOnClickListener {
            // TODO 북마크 클릭 시
        }

        dialog.findViewById<LinearLayout>(R.id.ll_Share).setOnClickListener {
            // TODO 공유 클릭 시
        }

        dialog.findViewById<LinearLayout>(R.id.ll_Report).setOnClickListener {
            // TODO 신고 클릭 시
        }
    }
}