package com.hand.comeeatme.view.main.user.edit

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.FragmentUserEditBinding
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.main.home.newpost.NewPostViewModel
import com.hand.comeeatme.view.main.home.newpost.album.AlbumActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserEditFragment : BaseFragment<UserEditViewModel, FragmentUserEditBinding>() {
    companion object {
        const val TAG = "UserEditFragment"
        const val PROFILE = "profile"
        const val NICKNAME = "nickname"
        const val INTRODUCTION = "introduction"

        fun newInstance(
            profile: String?,
            nickname: String?,
            introduction: String?,
        ): UserEditFragment {
            val bundle = bundleOf(
                PROFILE to profile,
                NICKNAME to nickname,
                INTRODUCTION to introduction
            )

            return UserEditFragment().apply {
                arguments = bundle
            }
        }

    }

    private val profile by lazy {
        arguments?.getString(PROFILE)
    }
    private val nickname by lazy {
        arguments?.getString(NICKNAME)
    }
    private val introduction by lazy {
        arguments?.getString(INTRODUCTION)
    }

    private val REQ_STORAGE_PERMISSION = 201

    private val selectPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.getStringArrayListExtra(NewPostViewModel.COMPRESSED_PHOTO_KEY)?.let {
                viewModel.setResultPhotoList(it)
            }
        }
    override val viewModel by viewModel<UserEditViewModel>()
    override fun getViewBinding(): FragmentUserEditBinding =
        FragmentUserEditBinding.inflate(layoutInflater)

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun observeData() {
        viewModel.userEditStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is UserEditState.Uninitialized -> {
                    if (profile.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(R.drawable.default_profile)
                            .into(binding.clProfile)
                    } else {
                        Glide.with(requireContext())
                            .load(profile)
                            .into(binding.clProfile)
                    }

                    binding.etNickName.setText(nickname)
                    binding.etIntroduce.setText(introduction)
                }

                is UserEditState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is UserEditState.CompressPhotoFinish -> {
                    if(it.compressPhotoList[0].isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(R.drawable.default_profile).apply(
                                RequestOptions()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                            )
                            .into(binding.clProfile)
                    } else {
                        Glide.with(requireContext())
                            .load(it.compressPhotoList[0]).apply(
                                RequestOptions()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                            )
                            .into(binding.clProfile)
                    }
                }

                is UserEditState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    finish()
                }

                is UserEditState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        clProfileEdit.setOnClickListener {
            val readPermission = ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (readPermission == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQ_STORAGE_PERMISSION
                )
            } else {
                selectPhotoLauncher.launch(
                    AlbumActivity.newIntent(requireContext(), true)
                )
            }
        }

        ibPrev.setOnClickListener {
            finish()
        }

        tvFinish.setOnClickListener {
            if(viewModel.getCompressPhotoPathList().isNullOrEmpty()) {

            }

            viewModel.modifyMemberInformation("${etNickName.text}", "${etIntroduce.text}")
        }



        ibIntroduce.setOnClickListener {
            binding.etIntroduce.text = null

        }

        ibNickName.setOnClickListener {
            binding.etNickName.text = null
        }


    }

    private fun finish() {
        val manager: FragmentManager? = activity?.supportFragmentManager
        val ft: FragmentTransaction = manager!!.beginTransaction()

        val fragment = manager.findFragmentByTag(TAG)

        if (fragment != null) {
            ft.remove(fragment)
        }

        ft.commitAllowingStateLoss()
    }

}