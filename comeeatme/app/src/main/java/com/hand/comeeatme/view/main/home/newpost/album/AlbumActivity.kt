package com.hand.comeeatme.view.main.home.newpost.album

import AlbumAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hand.comeeatme.R
import com.hand.comeeatme.data.Thumbnail
import com.hand.comeeatme.databinding.ActivityAlbumBinding
import com.hand.comeeatme.view.base.BaseActivity
import com.hand.comeeatme.view.main.home.newpost.NewPostFragment
import com.hand.comeeatme.view.main.home.newpost.NewPostViewModel
import com.hand.comeeatme.view.main.home.newpost.crop.CropActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class AlbumActivity : BaseActivity<AlbumViewModel, ActivityAlbumBinding>() {
    companion object {
        const val IS_PROFILE = "isProfile"
        fun newIntent(context: Context, isProfile: Boolean) =
            Intent(context, AlbumActivity::class.java).putExtra(IS_PROFILE, isProfile)
    }

    private val isProfile by lazy {
        intent.getBooleanExtra(IS_PROFILE, false)
    }

    override fun getViewBinding(): ActivityAlbumBinding =
        ActivityAlbumBinding.inflate(layoutInflater)

    override val viewModel by viewModel<AlbumViewModel>()

    private lateinit var adapter: AlbumAdapter

    override fun observeData() = viewModel.albumStateLiveData.observe(this) {
        when (it) {
            is AlbumState.PhotoUnReady -> {
                binding.tvNext.setTextColor(ContextCompat.getColor(applicationContext,
                    R.color.nonCheck))
            }

            is AlbumState.PhotoReady -> {
                binding.tvNext.setTextColor(ContextCompat.getColor(applicationContext,
                    R.color.basic))
            }

            is AlbumState.Initialized -> {
                setAdapter(viewModel.getThumbnailList())
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 100) {
            val intent = Intent(this, NewPostFragment::class.java)
            intent.putExtra(NewPostViewModel.COMPRESSED_PHOTO_KEY,
                data!!.getStringArrayListExtra(NewPostViewModel.COMPRESSED_PHOTO_KEY) as ArrayList<String>)
            setResult(RESULT_OK, intent)
            finish()


        }
    }

    override fun initView() = with(binding) {
        tvNext.setOnClickListener {
            if (tvNext.currentTextColor == ContextCompat.getColor(applicationContext,
                    R.color.nonCheck)
            ) {
                Toast.makeText(applicationContext, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else if (tvNext.currentTextColor == ContextCompat.getColor(application,
                    R.color.basic)
            ) {
                startActivityForResult(CropActivity.newIntent(applicationContext,
                    viewModel.getCheckedPhotoList()), 100)
            }
        }

        ibPrev.setOnClickListener {
            finish()
        }

//        toolbarNewProject.setNavigationOnClickListener {
//            val intent = Intent(this, NewPostFragment::class.java)
//            intent.putExtra("checkedImages", checkedImageList)
//            intent.putExtra("imagePosition", imagePositionList)
//            intent.putExtra("cropImages", cropImageList)
//            setResult(RESULT_OK, intent)
//        }

        getImage()
    }


    private fun getImage() {
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA
        )

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " ASC"
        )

        val thumbnailList = ArrayList<Thumbnail>()

        for (i in cursor!!.count - 1 downTo 0) {
            cursor!!.moveToPosition(i)
            val id = cursor!!.getLong(0)

            val thumbnail = getThumbnail(id)
            val path =
                File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))).toString()
            val uri =
                Uri.fromFile(File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))))


            val sampleImage = Thumbnail(cursor!!.count - 1 - i, thumbnail, uri, path, null, false)

            thumbnailList.add(sampleImage)
        }
        cursor.close()

        viewModel.setThumbnailList(thumbnailList)
    }

    private fun setAdapter(thumbnails: ArrayList<Thumbnail>) {
        adapter = AlbumAdapter(
            thumbnails,
            onClickImage = {
                addCheckedImage(it)
            },
            onCheckedImage = {
                removeCheckedImage(it)
            }
        )
        binding.rvPhoto.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun addCheckedImage(photo: Thumbnail) {
        if (isProfile) {
            viewModel.removeAllCheckedPhoto()
            viewModel.addCheckedPhotoItem(photo.path)
            startActivityForResult(CropActivity.newIntent(applicationContext,
                viewModel.getCheckedPhotoList()), 100)
        } else {
            if (viewModel.getCheckedPhotoList().size < 10) {
                viewModel.getThumbnailList()[photo.position].isChecked = true
                viewModel.addCheckedPhotoItem(photo.path)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "최대 10장의 사진을 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun removeCheckedImage(photo: Thumbnail) {
        viewModel.getThumbnailList()[photo.position].isChecked = false
        viewModel.removeCheckedPhotoItem(photo.path)
        adapter.notifyDataSetChanged()
    }


    private fun getThumbnail(id: Long): Bitmap {
        val options = BitmapFactory.Options()
        options.inDither = true
        options.inSampleSize = 2
        options.inPreferredConfig = Bitmap.Config.RGB_565

        var bit = MediaStore.Images.Thumbnails.getThumbnail(
            contentResolver,
            id,
            MediaStore.Images.Thumbnails.MINI_KIND,
            options
        )

        return ThumbnailUtils.extractThumbnail(bit, 100, 100, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
    }


}