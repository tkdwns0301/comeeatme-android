package com.hand.comeeatme.util.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.LayoutHomeItemBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment

class UserListAdapter(
    private val items: ArrayList<ArrayList<Int>>,
    private val context: Context
    ) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.viewPager.adapter = ViewPagerAdapter(items[position], context)
        holder.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        holder.container.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, DetailPostFragment(), "fm_Post")
            ft.commitAllowingStateLoss()
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: LayoutHomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container = binding.clHomeItem
        val viewPager = binding.vpPhotos
    }
}