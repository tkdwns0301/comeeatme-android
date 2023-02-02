package com.hand.comeeatme.view.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityMainBinding
import com.hand.comeeatme.util.event.MenuChangeEventBus
import com.hand.comeeatme.view.main.bookmark.BookmarkFragment
import com.hand.comeeatme.view.main.home.HomeFragment
import com.hand.comeeatme.view.main.home.newpost.NewPostFragment
import com.hand.comeeatme.view.main.home.post.DetailPostFragment
import com.hand.comeeatme.view.main.rank.RankFragment
import com.hand.comeeatme.view.main.user.UserFragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val SCHEME_POSTID = "postId"

        const val PARAM_ID = "id"

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()
        initView()
    }

    private fun observeData() {
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect {
                goToTab(it)
            }
        }
    }

    private fun initView() = with(binding) {
        bnMain.setOnNavigationItemSelectedListener(this@MainActivity)
        setFragment(HomeFragment.TAG, HomeFragment.newInstance())

        binding.ibNewPost.setOnClickListener {
            onNewPost()
        }

        handleDynamicLinks()
    }

    private fun goToTab(mainTabMenu: MainTabMenu) {
        binding.bnMain.selectedItemId = mainTabMenu.menuId
    }

    private fun onNewPost() {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        ft.add(R.id.fg_MainContainer, NewPostFragment.newInstance(false, null), NewPostFragment.TAG)

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
        val transaction = supportFragmentManager.beginTransaction()
        val findFragment = supportFragmentManager.findFragmentByTag(tag)

        supportFragmentManager.fragments.forEach { fm ->
            transaction.hide(fm)
        }

        findFragment?.let {
            transaction.show(it)
        } ?: kotlin.run {
            transaction
                .add(R.id.fg_MainContainer, fragment, tag)
        }

        transaction.commitAllowingStateLoss()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fm_Home -> {
                setFragment(HomeFragment.TAG, HomeFragment.newInstance())
                true
            }
            R.id.fm_Rank -> {
                setFragment(RankFragment.TAG, RankFragment.newInstance())
                true
            }
            R.id.fm_Bookmark -> {
                setFragment(BookmarkFragment.TAG, BookmarkFragment.newInstance())
                true
            }
            R.id.fm_User -> {
                setFragment(UserFragment.TAG, UserFragment.newInstance())
                true
            }
            else -> true
        }
    }

    private fun handleDynamicLinks() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                if (deepLink != null) {
                    when (deepLink.lastPathSegment!!) {
                        SCHEME_POSTID -> {
                            val postId: String = deepLink.getQueryParameter(PARAM_ID)!!
                            Log.e("postId", "$postId")

                            val manager: FragmentManager = supportFragmentManager
                            val ft: FragmentTransaction = manager.beginTransaction()

                            val findFragment = supportFragmentManager.findFragmentByTag(
                                DetailPostFragment.TAG)

                            findFragment?.let {
                                manager.beginTransaction().remove(it).commitAllowingStateLoss()
                            }

                            ft.add(R.id.fg_MainContainer,
                                DetailPostFragment.newInstance(postId.toLong()),
                                DetailPostFragment.TAG)
                            ft.addToBackStack(DetailPostFragment.TAG).commitAllowingStateLoss()
                        }
                    }
                } else {
                    Log.e("handleDynamicLink", "No Link Found")
                }
            }
            .addOnFailureListener(this) { e ->
                Log.e(HomeFragment.TAG, "getDynamicLink:onFailure", e)
            }
    }
}

enum class MainTabMenu(@IdRes val menuId: Int) {
    HOME(R.id.fm_Home), MAP(R.id.fm_Rank), BOOKMARK(R.id.fm_Bookmark), USER(R.id.fm_User)
}