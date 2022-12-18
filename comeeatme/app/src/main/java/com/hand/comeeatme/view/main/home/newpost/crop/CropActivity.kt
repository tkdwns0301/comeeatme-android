package com.hand.comeeatme.view.main.home.newpost.crop

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityCropBinding
import com.hand.comeeatme.util.FileUtil
import com.hand.comeeatme.util.widget.adapter.CropAdapter
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.main.home.newpost.NewPostViewModel
import com.takusemba.cropme.OnCropListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class CropActivity : BaseActivity<CropViewModel, ActivityCropBinding>() {
    companion object {
        fun newIntent(context: Context, checkedPhotoList: ArrayList<String>) =
            Intent(context, CropActivity::class.java)
                .putExtra(NewPostViewModel.CHECKED_PHOTOS_KEY, checkedPhotoList)
    }

    override val viewModel by viewModel<CropViewModel>()
    override fun getViewBinding(): ActivityCropBinding = ActivityCropBinding.inflate(layoutInflater)

    private lateinit var adapter: CropAdapter

    private val visited = ArrayList<Boolean>()
    private var image: String? = null
    private var index = 0

    override fun observeData() = viewModel.cropStateLiveData.observe(this) {
        when(it) {
            is CropState.Loading -> {
                binding.clLoading.isVisible = true
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            is CropState.Initialized -> {
                for(i in 0 until viewModel.getCheckedPhotoList().size) {
                    visited.add(false)
                }
                setAdapter()
            }

            is CropState.CompressFinish -> {
                binding.clLoading.isGone = true
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                intent.putExtra(NewPostViewModel.COMPRESSED_PHOTO_KEY, it.compressPhotoPathList)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun initView() = with(binding) {
        Glide.with(applicationContext)
            .load(R.drawable.loading)
            .into(ivLoading)

        window.statusBarColor = Color.parseColor("#333333")
        rvCheckedImageList.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        intent.getStringArrayListExtra(NewPostViewModel.CHECKED_PHOTOS_KEY)
            ?.let { viewModel.setCheckedPhotoList(it) }

        ibPrev.setOnClickListener {
            finish()
        }

        tvFinish.setOnClickListener {
            viewModel.compressPhoto(applicationContext)

        }

        clCrop.setOnClickListener {
            clCropImage.crop()
        }

        clCropImage.addOnCropListener(object : OnCropListener {
            override fun onSuccess(bitmap: Bitmap) {
                val compressPhotoPath = FileUtil.optimizeBitmap(bitmap, viewModel.getCheckedPhotoList().indexOf(image))

                viewModel.addCompressPhotoPath(compressPhotoPath!!, viewModel.getCheckedPhotoList().indexOf(image))

                Toast.makeText(applicationContext, "이미지 편집을 성공하였습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(e: Exception) {
                Log.e("Failure", "$e")
                Toast.makeText(applicationContext, "이미지를 화면에 맞추어 편집해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })


    }

    private fun setAdapter() {
        adapter = CropAdapter(
            applicationContext,
            viewModel.getCheckedPhotoList(),
            onClickImage = {
                setCropImage(it)
            },
            visited
        )
        binding.rvCheckedImageList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setCropImage(imagePath: String) {
        visited[index] = false
        visited[viewModel.getCheckedPhotoList().indexOf(imagePath)] = true
        index = viewModel.getCheckedPhotoList().indexOf(imagePath)
        adapter.notifyDataSetChanged()

        image = imagePath

        binding.clCropImage.setBitmap(rotateImageIfRequired(uriToBitmap(Uri.fromFile(File(imagePath)))!!, Uri.fromFile(File(imagePath)))!!)

        //binding.clCropImage.setUri(Uri.fromFile(File(imagePath)))
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        } catch (e: Exception) {
            Log.e("UriToBitmap Error",  "${e.printStackTrace()}")
        }

        return bitmap
    }

    private fun rotateImageIfRequired(bitmap: Bitmap, uri: Uri) : Bitmap? {
        val input = applicationContext.contentResolver.openInputStream(uri)?: return null

        val exif = if(Build.VERSION.SDK_INT > 23) {
            ExifInterface(input)
        } else {
            ExifInterface(uri.path!!)
        }

        return when(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()

        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}