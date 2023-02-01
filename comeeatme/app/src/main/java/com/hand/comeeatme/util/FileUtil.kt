package com.hand.comeeatme.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
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

    suspend fun resizeBitmap(
        context: Context,
        photoPathList: ArrayList<String>,
        compressPhotoPathMap: HashMap<Int, String>,
    ): MutableList<String> {

        CoroutineScope(Dispatchers.IO).launch {
            photoPathList.forEachIndexed { index, photoPath ->
                if (!compressPhotoPathMap.containsKey(index)) {
                    launch {
                        cropBitmap(context, photoPath, index)?.let {
                            compressPhotoPathMap[index] = it
                        }
                    }
                }
            }
        }.join()

        return compressPhotoPathMap.values.toMutableList()
    }

    private fun cropBitmap(context: Context, imagePath: String, position: Int): String? {
        var bitmap: Bitmap? = null

        try {
            bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(appContext!!.contentResolver,
                    Uri.fromFile(File(imagePath))))
            } else {
                MediaStore.Images.Media.getBitmap(appContext!!.contentResolver,
                    Uri.fromFile(File(imagePath)))
            }
        } catch (e: Exception) {
            Log.e("GetBitMap Error", "${e.printStackTrace()}")
        }

        bitmap = rotateImageIfRequired(context, bitmap!!, Uri.fromFile(File(imagePath)))

        val width = bitmap!!.width
        val height = bitmap!!.height

        var cropBitmap: Bitmap = if (width > height) {
            val x = (width - height) / 2
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

    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(uri) ?: return null

        val exif = if (Build.VERSION.SDK_INT > 23) {
            ExifInterface(input)
        } else {
            ExifInterface(uri.path!!)
        }

        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
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