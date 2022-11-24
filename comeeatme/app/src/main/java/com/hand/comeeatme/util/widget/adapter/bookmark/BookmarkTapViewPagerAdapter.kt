package com.hand.comeeatme.util.widget.adapter.bookmark

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BookmarkTapViewPagerAdapter(
    fragment: Fragment,
    val fragmentList: List<Fragment>
) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}