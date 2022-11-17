package com.hand.comeeatme.view.main.home.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.FragmentPostBinding
import com.hand.comeeatme.view.dialog.PostDialog
import com.hand.comeeatme.view.main.MainActivity

class PostFragment: Fragment(R.layout.fragment_post), MainActivity.onBackPressedListener {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)

        initListener()

        return binding.root
    }

    private fun initListener() {
        binding.toolbarPost.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.toolbar_Menu -> {
                    // TODO Dialog 띄워서 신고하기 등등 보여주기
                    PostDialog(requireContext()).showDialog()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        binding.toolbarPost.setNavigationOnClickListener {
            Log.e("이전버튼" ,"클릭")
            finish()
        }
    }

    private fun finish() {
        val manager: FragmentManager? = activity?.supportFragmentManager
        val ft: FragmentTransaction = manager!!.beginTransaction()

        val post = manager.findFragmentByTag("fm_Post")

        if (post != null) {
            ft.remove(post)
        }

        ft.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }
}