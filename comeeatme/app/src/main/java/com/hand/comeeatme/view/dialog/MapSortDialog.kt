package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.TextView
import com.hand.comeeatme.R

class MapSortDialog(private val context: Context) {
    private val dialog = Dialog(context)


    fun showDialog() {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_map_sort)

        val width = context.resources.displayMetrics.widthPixels

        dialog.window!!.setLayout(
            (width - 200),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.findViewById<TextView>(R.id.tv_Dist).setOnClickListener {
            // TODO 거리순 클릭 시
        }

        dialog.findViewById<TextView>(R.id.tv_Review).setOnClickListener {
            // TODO 공유 클릭 시
        }
    }
}