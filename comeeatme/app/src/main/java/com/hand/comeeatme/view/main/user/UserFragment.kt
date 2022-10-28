package com.hand.comeeatme.view.main.user

import UserGridAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hand.comeeatme.R
import com.hand.comeeatme.adapter.UserListAdapter
import com.hand.comeeatme.databinding.FragmentUserBinding
import com.hand.comeeatme.view.dialog.UserSortDialog

class UserFragment: Fragment(R.layout.fragment_user) {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterList: UserListAdapter
    private lateinit var adapterGrid: UserGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        initView()
        initListener()

        return binding.root
    }

    private fun initView() {
        val items = ArrayList<ArrayList<Int>>()

        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))
        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))
        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))
        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))
        items.add(arrayListOf<Int>(R.drawable.food1, R.drawable.food2))


        if(items == null) {
            binding.rvGrid.visibility = View.INVISIBLE
            binding.rvList.visibility = View.INVISIBLE
            binding.clNonPost.visibility = View.VISIBLE
        } else {
            binding.clNonPost.visibility = View.INVISIBLE
            if(binding.rbGrid.isChecked) {
                binding.rvGrid.visibility = View.VISIBLE
                binding.rvList.visibility = View.INVISIBLE
            } else if(binding.rbList.isChecked) {
                binding.rvGrid.visibility = View.INVISIBLE
                binding.rvList.visibility = View.VISIBLE
            }
            adapterGrid = UserGridAdapter(items, requireContext())
            adapterList = UserListAdapter(items, requireContext())

            binding.rvGrid.adapter = adapterGrid
            binding.rvList.adapter = adapterList

            adapterGrid.notifyDataSetChanged()
            adapterList.notifyDataSetChanged()
        }
    }

    private fun initListener() {
        binding.rgListAndGrid.setOnCheckedChangeListener { group, checkId ->
            when(checkId) {
                R.id.rb_Grid -> {
                    binding.rvList.visibility = View.INVISIBLE
                    binding.rvGrid.visibility = View.VISIBLE
                } else -> {
                    binding.rvList.visibility = View.VISIBLE
                    binding.rvGrid.visibility = View.INVISIBLE
                }
            }

        }

        binding.ivName.setOnClickListener {
            val manager: FragmentManager = requireActivity().supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, UserEditFragment(), "fm_UserEdit")
            ft.commitAllowingStateLoss()
        }

        binding.clSort.setOnClickListener {
            UserSortDialog(requireContext()).showDialog()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}