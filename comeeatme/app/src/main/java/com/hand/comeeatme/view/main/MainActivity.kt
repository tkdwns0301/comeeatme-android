package com.hand.comeeatme.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityMainBinding
import com.hand.comeeatme.view.main.home.HomeFragment
import com.hand.comeeatme.view.main.home.NewPostFragment
import com.hand.comeeatme.view.main.map.MapFragment


private const val TAG_HOME = "fm_Home"
private const val TAG_MAP = "fm_Map"
private const val TAG_POST = "fm_Post"
private const val TAG_NEWPOST = "fm_NewPost"
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
                R.id.fm_Map -> setFragment(TAG_MAP, MapFragment())

            }
            true
        }

        initListener()


    }


    private fun initListener() {
        binding.ibNewPost.setOnClickListener {
            Log.e("NewPost", "clicked")
            onNewPost()
        }
    }

    private fun onNewPost() {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        ft.add(R.id.fg_MainContainer, NewPostFragment(), "fm_NewPost")

        ft.commitAllowingStateLoss()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        if (currentFocus is EditText) {
            currentFocus!!.clearFocus()
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.fg_MainContainer, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val map = manager.findFragmentByTag(TAG_MAP)
        val newPost = manager.findFragmentByTag(TAG_NEWPOST)
        val post = manager.findFragmentByTag(TAG_POST)

        if (home != null) {
            ft.hide(home)
        }

        if (map != null) {
            ft.hide(map)
        }

        if (newPost != null) {
            ft.remove(newPost)
        }

        if (post != null) {
            ft.remove(post)
        }

        if (tag == TAG_HOME) {
            if (home != null) {
                ft.show(home)
            }
        } else if (tag == TAG_MAP) {
            if (map != null) {
                ft.show(map)
            }
        }
        ft.commitAllowingStateLoss()
    }

    interface onBackPressedListener {
        fun onBackPressed()
    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments

        if(fragmentList.size != 1) {
            for (fragment in fragmentList) {
                if (fragment is onBackPressedListener) {
                    (fragment as onBackPressedListener).onBackPressed()
                    return
                }
            }
        } else {
            super.onBackPressed()
        }

    }
}