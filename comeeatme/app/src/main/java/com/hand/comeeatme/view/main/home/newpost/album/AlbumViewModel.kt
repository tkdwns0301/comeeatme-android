package com.hand.comeeatme.view.main.home.newpost.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.Thumbnail
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class AlbumViewModel : BaseViewModel() {
    val albumStateLiveData = MutableLiveData<AlbumState>(AlbumState.UnInitialized)

    private var checkedPhotoList: ArrayList<String> = arrayListOf()
    private var thumbnailList: ArrayList<Thumbnail> = arrayListOf()

    fun setThumbnailList(thumbnails: ArrayList<Thumbnail>) = viewModelScope.launch {
        thumbnailList = thumbnails
        albumStateLiveData.value = AlbumState.Initialized(
            thumbnailList = thumbnailList
        )
    }

    fun getThumbnailList(): ArrayList<Thumbnail> {
        return thumbnailList
    }


    fun addCheckedPhotoItem(photoPath: String) = viewModelScope.launch {
        checkedPhotoList.add(photoPath)

        albumStateLiveData.value = AlbumState.PhotoReady

    }

    fun removeCheckedPhotoItem(photoPath: String) = viewModelScope.launch {
        checkedPhotoList.remove(photoPath)

        if (checkedPhotoList.isEmpty()) {
            albumStateLiveData.value = AlbumState.PhotoUnReady
        }
    }

    fun removeAllCheckedPhoto() {
        checkedPhotoList = arrayListOf()
    }

    fun getCheckedPhotoList(): ArrayList<String> {
        return checkedPhotoList
    }

}