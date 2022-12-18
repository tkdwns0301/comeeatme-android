package com.hand.comeeatme.view.main.home.newpost.crop

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.util.FileUtil
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.*

class CropViewModel : BaseViewModel() {
    val cropStateLiveData = MutableLiveData<CropState>(CropState.Uninitialized)

    private var compressPhotoMap: HashMap<Int, String> = hashMapOf()
    private var compressPhotoList: ArrayList<String> = arrayListOf()
    private var checkedPhotoList: ArrayList<String> = arrayListOf()

    fun setCheckedPhotoList(photos: ArrayList<String>) = viewModelScope.launch {
        checkedPhotoList = photos

        cropStateLiveData.value = CropState.Initialized(
            checkedPhotoList = checkedPhotoList
        )
    }

    fun getCheckedPhotoList(): ArrayList<String> {
        return checkedPhotoList
    }


    fun addCompressPhotoPath(compressPhotoPath: String, position: Int) {
        compressPhotoMap[position] = compressPhotoPath
    }

    fun getCompressPhotoPath(): HashMap<Int, String> {
        return compressPhotoMap
    }

    fun compressPhoto(context: Context) = viewModelScope.launch {
        cropStateLiveData.value = CropState.Loading
        compressPhoto2(context)

        cropStateLiveData.value = CropState.CompressFinish(
            compressPhotoPathList = compressPhotoList
        )
    }

    private suspend fun compressPhoto2(context: Context) = withContext(Dispatchers.IO) {
        val compress = FileUtil.resizeBitmap(context, checkedPhotoList, compressPhotoMap).mapIndexed { index, path ->
            viewModelScope.async {
                try {
                    return@async compressPhotoList
                        .add(path)
                } catch ( e: Exception) {
                    cropStateLiveData.value = CropState.Error
                    return@async Pair(path, e)
                }
            }
        }

        return@withContext compress.awaitAll()
    }

}