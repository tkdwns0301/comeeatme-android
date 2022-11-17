package com.hand.comeeatme.util

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.hand.comeeatme.ComeEatMeApplication.Companion.appContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

object FileUtil {


    suspend fun resizeBitmap(photoPathList: ArrayList<String>, compressPhotoPathMap: HashMap<Int, String>): MutableList<String> {

        CoroutineScope(Dispatchers.IO).launch {
            Log.e("1", "1")
            photoPathList.forEachIndexed { index, photoPath ->
                Log.e("2", "$index, $photoPath")
                if (!compressPhotoPathMap.containsKey(index)) {
                    Log.e("3", "3")
                    launch {
                        Log.e("resize", "$index, $photoPath")
                        cropBitmap(photoPath, index)?.let {
                            compressPhotoPathMap[index] = it
                        }
                    }
                }
            }
        }.join()

        Log.e("asdasd", "asdasd")

        return compressPhotoPathMap.values.toMutableList()
    }

    private fun cropBitmap(imagePath: String, position: Int): String? {
        var bitmap: Bitmap? = null

        try {
            bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(appContext!!.contentResolver, Uri.fromFile(File(imagePath))))
            } else {
                MediaStore.Images.Media.getBitmap(appContext!!.contentResolver, Uri.fromFile(File(imagePath)))
            }
        } catch (e: Exception) {
            Log.e("GetBitMap Error", "${e.printStackTrace()}")
        }

        val width = bitmap!!.width
        val height = bitmap!!.height

        var cropBitmap: Bitmap = if(width > height) {
            val x = (width - height) /2
            val y = 0
            Bitmap.createBitmap(bitmap, x, y, height, height)
        } else {
            val x = 0
            val y = (height - width) / 2
            Bitmap.createBitmap(bitmap, x, y, width, width)
        }

        return optimizeBitmap(cropBitmap, position)
    }

    fun optimizeBitmap(bitmap: Bitmap, position: Int): String? {
        try {
            val storage = appContext!!.cacheDir
            val fileName = String.format("%d.%s", position, "webp")

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

}