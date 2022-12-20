package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.hand.comeeatme.databinding.DialogRankSortBinding

class RankSortDialog(
    context: Context,
    val postSort: (sort: String) -> Unit,
    val favoriteSort: (sort: String) -> Unit,
) : Dialog(context) {
    private lateinit var binding: DialogRankSortBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogRankSortBinding.inflate(layoutInflater)
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

        tvPost.setOnClickListener {
            postSort.invoke("postCount,desc")
            dismiss()
        }

        tvFavorite.setOnClickListener {
            favoriteSort.invoke("favoriteCount,desc")
            dismiss()
        }
    }
}