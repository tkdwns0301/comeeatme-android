package com.hand.comeeatme.view.main.home.newpost.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.ComeEatMeApplication.Companion.appContext
import com.hand.comeeatme.data.Thumbnail
import com.hand.comeeatme.data.Thumbnail2
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AlbumViewModel : BaseViewModel() {
    val albumStateLiveData = MutableLiveData<AlbumState>(AlbumState.UnInitialized)

    private val galleryPhotoRepository by lazy {
        GalleryPhotoRepository(appContext!!)
    }

    private var checkedPhotoList: ArrayList<String> = arrayListOf()
    private var thumbnailList: ArrayList<Thumbnail> = arrayListOf()

    private lateinit var photoList : MutableList<Thumbnail2>

    override fun fetchData(): Job = viewModelScope.launch {
        albumStateLiveData.value = AlbumState.Loading
        photoList = galleryPhotoRepository.getAllPhotos()
        albumStateLiveData.value = AlbumState.Success(
            photoList = photoList
        )

    }

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