package com.hand.comeeatme.view.main.user

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityFollowBinding

class FollowActivity: AppCompatActivity() {
    private var _binding: ActivityFollowBinding? = null
    private val binding get() = _binding!!
    private var isFollowerView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFollowerView = intent.getBooleanExtra("isFollowerView", false)
        Log.e("isFollowerView", "$isFollowerView")


        initView()
        initListener()
    }

    private fun initView() {
        if(isFollowerView) {
            binding.rbFollower.isChecked = true
            binding.rbFollowing.isChecked = false
            binding.tvFollow.text = "회원님을 팔로잉하는\n사용자를 아직 찾지 못했어요."
        } else {
            binding.rbFollower.isChecked = false
            binding.rbFollowing.isChecked = true
            binding.tvFollow.text = "회원님이 팔로잉하는\n사용자를 아직 찾지 못했어요."
        }
    }

    private fun initListener() {
        binding.toolbarFollower.setNavigationOnClickListener {
            finish()
        }

        binding.rgFollowerAndFollowing.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId) {
                R.id.rb_Follower -> {
                    binding.tvFollow.text = "회원님을 팔로잉하는\n사용자를 아직 찾지 못했어요."
                }
                R.id.rb_Following -> {
                    binding.tvFollow.text = "회원님이 팔로잉하는\n사용자를 아직 찾지 못했어요."
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
