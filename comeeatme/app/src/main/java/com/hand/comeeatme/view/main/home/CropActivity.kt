package com.hand.comeeatme.view.main.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.data.CropImage
import com.hand.comeeatme.databinding.ActivityCropBinding
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener
import java.io.File
import java.io.FileOutputStream

class CropActivity : AppCompatActivity() {
    private var _binding: ActivityCropBinding? = null
    private val binding get() = _binding!!

    private lateinit var cancel: ImageButton
    private lateinit var save: ImageButton
    private lateinit var cropLayout: CropLayout

    private var checkedImageList: ArrayList<String>? = null
    private var cropImageList: ArrayList<String>? = null
    private var imagePositionList: ArrayList<Int>? = null
    private var images: ArrayList<CropImage> = ArrayList()
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkedImageList = intent.getStringArrayListExtra("checkedImages") as ArrayList<String>
        imagePositionList = intent.getIntegerArrayListExtra("imagePosition") as ArrayList<Int>
        cropImageList = intent.getStringArrayListExtra("cropImages") as ArrayList<String>

        for (i in 0 until checkedImageList!!.size) {
            val image = CropImage(Uri.fromFile(File(checkedImageList!![i])), false)
            images.add(image)
        }


        initView()
        initListener()
    }

    private fun initView() {
        cancel = binding.ibCancel
        save = binding.ibSave
        cropLayout = binding.clCropImage
        cropLayout.setUri(Uri.fromFile(File(checkedImageList!![index])))

    }

    private fun initListener() {
        cancel.setOnClickListener {
            // TODO 모든 사진의 crop이 완료되었을 때


            // TODO 완료되지 않았을 때

            val intent = Intent(this, Album2Activity::class.java)
            intent.putExtra("checkedImages", checkedImageList)
            intent.putExtra("imagePosition", imagePositionList)
            intent.putExtra("cropImages", cropImageList)
            setResult(RESULT_OK, intent)
            finish()
        }

        save.setOnClickListener {
            //TODO imageCrop
            cropLayout.crop()
            Log.e("Save", "click!!")

        }

//        binding.btnNext.setOnClickListener {
//            if (index + 1 < checkedImageList!!.size) {
//                cropLayout.setUri(Uri.fromFile(File(checkedImageList!![++index])))
//                binding.btnPrev.visibility = View.VISIBLE
//            }
//
//            if (index == checkedImageList!!.size-1) {
//                binding.btnNext.visibility = View.INVISIBLE
//            }
//
//        }
//
//        binding.btnPrev.setOnClickListener {
//            if (index - 1 >= 0) {
//                cropLayout.setUri(Uri.fromFile(File(checkedImageList!![--index])))
//                binding.btnNext.visibility = View.VISIBLE
//            }
//
//            if(index == 0) {
//                binding.btnPrev.visibility = View.INVISIBLE
//            }
//
//        }


        cropLayout.addOnCropListener(object : OnCropListener {
            override fun onSuccess(bitmap: Bitmap) {
                val resizeBitmap = bitmapResize(bitmap)
                val compressPath =
                    optimizeBitmap(applicationContext, resizeBitmap, index)

                cropImageList!!.add(compressPath!!)

                Toast.makeText(applicationContext, "이미지 편집을 성공", Toast.LENGTH_SHORT).show()

                if (index + 1 < checkedImageList!!.size) {
                    Log.e("asdasd", "Asdasd")
                    cropLayout.setUri(Uri.fromFile(File(checkedImageList!![++index])))
                } else if(index == checkedImageList!!.size-1) {
                    val intent = Intent(applicationContext, Album2Activity::class.java)
                    intent.putExtra("checkedImages", checkedImageList)
                    intent.putExtra("imagePosition", imagePositionList)
                    intent.putExtra("cropImages", cropImageList)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

            override fun onFailure(e: Exception) {
                Log.e("Failure", "$e")
            }
        })

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}