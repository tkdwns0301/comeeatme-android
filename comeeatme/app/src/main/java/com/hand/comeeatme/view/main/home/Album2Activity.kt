package com.hand.comeeatme.view.main.home

import AlbumAdapter
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.R
import com.hand.comeeatme.data.SampleImage
import com.hand.comeeatme.databinding.ActivityAlbum2Binding
import java.io.File


class Album2Activity : AppCompatActivity() {
    private var _binding: ActivityAlbum2Binding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var adapter: AlbumAdapter


    private var position: Int = 0
    private val images = ArrayList<SampleImage>()
    private var checkedImageList: ArrayList<String>? = null
    private var imagePositionList: ArrayList<Int>? = null
    private var cropImageList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAlbum2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        checkedImageList = intent.getStringArrayListExtra("checkedImages") as ArrayList<String>
        imagePositionList = intent.getIntegerArrayListExtra("imagePosition") as ArrayList<Int>
        cropImageList = intent.getStringArrayListExtra("cropImages") as ArrayList<String>

        initView()
        initLiListener()
        getImage()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == AppCompatActivity.RESULT_OK && requestCode == 100) {
            cropImageList = data!!.getStringArrayListExtra("cropImages") as ArrayList<String>

            // TODO cache 파일 리스트 받아서 NewPostFragment 로 넘겨주기
            val intent = Intent(this, NewPostFragment::class.java)
            intent.putExtra("checkedImages", checkedImageList)
            intent.putExtra("imagePosition", imagePositionList)
            intent.putExtra("cropImages", cropImageList)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initView() {
        recyclerView = binding.rvSelectedImages
        toolbar = binding.toolbarNewProject
    }

    private fun initLiListener() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_Next -> {
                    val intent = Intent(this, CropActivity::class.java)
                    intent.putExtra("checkedImages", checkedImageList)
                    intent.putExtra("imagePosition", imagePositionList)
                    intent.putExtra("cropImages", cropImageList)
                    startActivityForResult(intent, 100)
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, NewPostFragment::class.java)
            intent.putExtra("checkedImages", checkedImageList)
            intent.putExtra("imagePosition", imagePositionList)
            intent.putExtra("cropImages", cropImageList)
            setResult(RESULT_OK, intent)
            finish()
        }

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

        for (i in 0 until cursor!!.count) {
            cursor!!.moveToPosition(i)
            val id = cursor!!.getLong(0)

            val thumbnail = getThumbnail(id)
            val path =
                File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))).toString()
            val uri =
                Uri.fromFile(File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))))


            val sampleImage = SampleImage(i, thumbnail, uri, path, false)

            if(imagePositionList!!.contains(i)) {
                sampleImage.isChecked = true
            }

            images.add(sampleImage)
        }

        cursor!!.close()
        setAdapter()
    }

    private fun setAdapter() {
        if (images != null) {
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            adapter = AlbumAdapter(
                images,
                onClickImage = {
                    addCheckedImage(it)
                },
                onCheckedImage = {
                    removeCheckedImage(it)
                }
            )
            recyclerView.adapter = adapter
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }
    }

    private fun addCheckedImage(image: SampleImage) {
        images[image.position].isChecked = true
        checkedImageList!!.add(image.path!!)
        imagePositionList!!.add(image.position)
        adapter.notifyDataSetChanged()
    }

    private fun removeCheckedImage(position: Int) {
        images[position].isChecked = false
        checkedImageList!!.remove(images[position].path)
        imagePositionList!!.remove(position)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}