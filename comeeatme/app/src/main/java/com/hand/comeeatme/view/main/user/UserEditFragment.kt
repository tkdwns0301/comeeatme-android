package com.hand.comeeatme.view.main.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.FragmentUserEditBinding

class UserEditFragment: Fragment(R.layout.fragment_user_edit) {
    private var _binding: FragmentUserEditBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserEditBinding.inflate(inflater, container, false)

        initView()
        initListener()
        return binding.root

    }

    private fun initView() {

    }

    private fun initListener() {
        binding.toolbarUserEdit.setNavigationOnClickListener {
            finish()
        }

        binding.ibIntroduce.setOnClickListener {
            binding.etIntroduce.text = null
            binding.etIntroduce.clearFocus()
        }

        binding.ibNickName.setOnClickListener {
            binding.etNickName.text = null
            binding.etNickName.clearFocus()
        }
    }

    private fun finish() {
        val manager: FragmentManager? = activity?.supportFragmentManager
        val ft: FragmentTransaction = manager!!.beginTransaction()

        val newPost = manager.findFragmentByTag("fm_UserEdit")

        if (newPost != null) {
            ft.remove(newPost)
        }

        ft.commitAllowingStateLoss()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}