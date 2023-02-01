package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.hand.comeeatme.databinding.DialogRestaurantPostSortBinding

class RestaurantPostSortDialog(
    context: Context,
    val recentSort: (sort: String) -> Unit,
    val likeSort: (sort: String) -> Unit,
) : Dialog(context) {
    private lateinit var binding: DialogRestaurantPostSortBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogRestaurantPostSortBinding.inflate(layoutInflater)
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

        tvRecent.setOnClickListener {
            recentSort.invoke("id,desc")
            dismiss()
        }

        tvLike.setOnClickListener {
            likeSort.invoke("likeCount,desc")
            dismiss()
        }
    }
}