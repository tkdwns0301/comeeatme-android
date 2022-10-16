package com.hand.comeeatme.view.main.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.R
import com.hand.comeeatme.adapter.CropAdapter
import com.hand.comeeatme.databinding.ActivityCrop2Binding
import com.takusemba.cropme.OnCropListener
import java.io.File
import java.io.FileOutputStream

class Crop2Activity : AppCompatActivity() {
    private var _binding: ActivityCrop2Binding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CropAdapter

    private var checkedImageList: ArrayList<String>? = null
    private var cropImageList: ArrayList<String>? = null
    private var imagePositionList: ArrayList<Int>? = null
    private val visited = ArrayList<Boolean>()
    private var image: String? = null
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCrop2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#333333")

        checkedImageList = intent.getStringArrayListExtra("checkedImages") as ArrayList<String>
        imagePositionList = intent.getIntegerArrayListExtra("imagePosition") as ArrayList<Int>
        cropImageList = intent.getStringArrayListExtra("cropImages") as ArrayList<String>

        for(i in 0 until checkedImageList!!.size) {
            visited.add(false)
        }



        recyclerView = binding.rvCheckedImageList
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.layoutManager = layoutManager

        binding.toolbarCrop.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_Next -> {
                    binding.clCropImage.crop()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        binding.toolbarCrop.setNavigationOnClickListener {
            for(i in 0 until checkedImageList!!.size) {
                if(!checkedImageList!!.contains("$i.webp")) {
                    var bitmap = imageCrop(checkedImageList!![i])
                    bitmap = bitmapResize(bitmap)
                    val compressPath = optimizeBitmap(applicationContext, bitmap, i)

                    if(!cropImageList!!.contains(compressPath!!)){
                        cropImageList!!.add(compressPath!!)
                    }
                }
            }

            cropImageList!!.sort()

            val intent = Intent(this, Album2Activity::class.java)
            intent.putExtra("checkedImages", checkedImageList)
            intent.putExtra("imagePosition", imagePositionList)
            intent.putExtra("cropImages", cropImageList)
            setResult(RESULT_OK, intent)
            finish()
        }

        setAdapter()

        initListener()

    }

    private fun initListener() {
        binding.clCropImage.addOnCropListener(object : OnCropListener {
            override fun onSuccess(bitmap: Bitmap) {
                val resizeBitmap = bitmapResize(bitmap)
                val compressPath =
                    optimizeBitmap(applicationContext, resizeBitmap, checkedImageList!!.indexOf(image))

                if(!cropImageList!!.contains(compressPath!!)){
                    cropImageList!!.add(compressPath!!)
                }

                Toast.makeText(applicationContext, "이미지 편집을 성공", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(e: Exception) {
                Log.e("Failure", "$e")
                Toast.makeText(applicationContext, "이미지를 화면에 맞추어 편집해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun imageCrop(imagePath: String): Bitmap {
        var bitmap: Bitmap? = null

        try {
            bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, Uri.fromFile(File(imagePath))))
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(File(imagePath)))
            }
        } catch(e: Exception) {
            Log.e("GetBitmap Error", "${e.printStackTrace()}")
        }

        val width = bitmap!!.width
        val height = bitmap!!.height


        var cropBitmap: Bitmap? = null

        cropBitmap = if(width > height) {
            val x = (width - height)/2
            val y = 0
            Bitmap.createBitmap(bitmap, x, y, height, height)
        } else {
            val x = 0
            val y = (height - width) / 2
            Bitmap.createBitmap(bitmap, x, y, width, width)
        }

        return cropBitmap
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

    private fun optimizeBitmap(context: Context, bitmap: Bitmap, index: Int): String? {
        try {
            val storage = context.cacheDir

            val fileName = String.format("%d.%s", index, "webp")

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

    private fun setAdapter() {
        if (checkedImageList!! != null) {
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            adapter = CropAdapter(checkedImageList!!,
                onClickImage = {
                    setCropImage(it)
                },
                visited
            )
            recyclerView.adapter = adapter
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setCropImage(imagePath: String) {
        visited[index] = false
        visited[checkedImageList!!.indexOf(imagePath)] = true
        index = checkedImageList!!.indexOf(imagePath)
        adapter.notifyDataSetChanged()
        image = imagePath
        binding.clCropImage.setUri(Uri.fromFile(File(imagePath)))
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}