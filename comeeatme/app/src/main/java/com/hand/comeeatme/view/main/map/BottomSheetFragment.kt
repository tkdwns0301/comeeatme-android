package com.hand.comeeatme.view.main.map

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hand.comeeatme.databinding.LayoutMapBottomsheetBinding
import com.hand.comeeatme.util.widget.adapter.BottomSheetAdapter
import com.hand.comeeatme.view.dialog.MapSortDialog


class BottomSheetFragment: BottomSheetDialogFragment() {
    private var _binding : LayoutMapBottomsheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BottomSheetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = LayoutMapBottomsheetBinding.inflate(inflater, container, false)

        initView()
        initListener()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }

        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight()
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun initView() {
        val list = ArrayList<String>()

        list.add("aaaa")
        list.add("bbb")
        list.add("ccc")
        list.add("ddd")
        list.add("eee")
        list.add("fff")

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvList.layoutManager = layoutManager

        if(list != null) {
            adapter = BottomSheetAdapter(list, requireContext())
            binding.rvList.adapter = adapter
            adapter.notifyDataSetChanged()
        }


    }

    private fun initListener() {
        binding.clSort.setOnClickListener {
            MapSortDialog(requireContext()).showDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}