package com.hand.comeeatme.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityMainBinding
import com.hand.comeeatme.view.main.home.HomeFragment

private const val TAG_HOME = "home_fragment"
private const val TAG_MAP = "fm_Map"
private const val TAG_BOOKMARK = "fm_Bookmark"
private const val TAG_USER = "fm_User"


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_HOME, HomeFragment())

        binding.bnMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fm_Home -> setFragment(TAG_HOME, HomeFragment())
            }
            true
        }


    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.fg_MainContainer, fragment, tag)
        }

        val home = manager.findFragmentByTag(tag)

        if (home != null) {
            ft.hide(home)
        }

        if (tag == TAG_HOME) {
            if (home != null) {
                ft.show(home)
            }
        }

        ft.commitAllowingStateLoss()
    }
}