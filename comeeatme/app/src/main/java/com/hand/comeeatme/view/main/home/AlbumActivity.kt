package com.hand.comeeatme.view.main.home

import AlbumAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.data.SampleImage
import com.hand.comeeatme.databinding.ActivityAlbumBinding
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable


class AlbumActivity : AppCompatActivity(), Serializable {
    private var _binding: ActivityAlbumBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlbumAdapter
    private lateinit var cropLayout: CropLayout
    private lateinit var add: Button
    private lateinit var end: Button

    private var position: Int = 0
    private val images = ArrayList<SampleImage>()
    private var checkedImageList: ArrayList<String>? = null
    private var imagePositionList: ArrayList<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkedImageList = intent.getStringArrayListExtra("checkedImages") as ArrayList<String>
        imagePositionList = intent.getIntegerArrayListExtra("imagePosition") as ArrayList<Int>

        Log.e("checkedImageList", "$checkedImageList")
        Log.e("imagePosition", "$imagePositionList")

        add = binding.btnAdd
        end = binding.btnEnd
        recyclerView = binding.rvSelectedImages
        cropLayout = binding.clCropImage

        getImage()
        initLiListener()

    }

    private fun initLiListener() {
        add.setOnClickListener {
            if(!imagePositionList!!.contains(position)) {
                imagePositionList!!.add(position)
            }

            cropLayout.crop()
        }

        end.setOnClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            intent.putExtra("checkedImages", checkedImageList)
            intent.putExtra("imagePosition", imagePositionList)
            setResult(RESULT_OK, intent)
            finish()
        }

        cropLayout.addOnCropListener(object : OnCropListener {
            override fun onSuccess(bitmap: Bitmap) {
                val resizeBitmap = bitmapResize(bitmap)
                val compressPath =
                    optimizeBitmap(applicationContext, resizeBitmap, position)

                if(!checkedImageList!!.contains(compressPath)) {
                    checkedImageList!!.add(compressPath!!)
                }

                Log.e("checkedImageList add", "$checkedImageList")

                //images[position].path = compressPath

                adapter.notifyDataSetChanged()
            }

            override fun onFailure(e: Exception) {
                Log.e("Failure", "$e")
            }
        })

    }


    private fun optimizeBitmap(context: Context, bitmap: Bitmap, size: Int): String? {
        try {
            val storage = context.cacheDir
            val fileName = String.format("%d.%s", size, "webp")

            val tempFile = File(storage, fileName)
            tempFile.createNewFile()

            val fos = FileOutputStream(tempFile)

            bitmap.apply {
                compress(Bitmap.CompressFormat.WEBP, 20, fos)
                recycle()
            }

            fos.flush()
            fos.close()

            return tempFile.absolutePath
        } catch (e: Exception) {
            Log.e("optimizeBitmap : ", "${e.message}")
        }

        return null
    }


    private fun bitmapResize(bitmap: Bitmap): Bitmap {
        val resizeWidth = 1080

        val aspectRatio = bitmap.height / bitmap.width
        val targetHeight = resizeWidth * aspectRatio

        val result = Bitmap.createScaledBitmap(bitmap, resizeWidth, targetHeight, false)

        if (result != bitmap) {
            bitmap.recycle()
        }

        return result

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
            val uri =
                Uri.fromFile(File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))))

            if (imagePositionList!!.contains(i)) {
                //val sampleImage = SampleImage(i, thumbnail, uri, "/data/user/0/com.hand.comeeatme/cache/$i.webp")
                //images.add(sampleImage)
            } else {
                //val sampleImage = SampleImage(i, thumbnail, uri,  null)
                //images.add(sampleImage)
            }


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
                    setCropMeLayout(it)
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

    private fun setCropMeLayout(image: SampleImage) {
        position = image.position
        cropLayout.setUri(image.image)
    }

    private fun removeCheckedImage(position: Int) {
        checkedImageList!!.remove(images[position].path)
        Log.e("checkedImageList remove", "$checkedImageList")
        imagePositionList!!.remove(position)
        //cropLayout.background = null
        //images[position].path = null
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