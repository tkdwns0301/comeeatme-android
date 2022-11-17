package com.hand.comeeatme.view.main.home.newpost.crop

sealed class CropState {
    object Uninitialized: CropState()

    object Loading: CropState()

    data class Initialized(
        val checkedPhotoList: ArrayList<String>,
    ): CropState()

    data class CompressFinish(
        val compressPhotoPathList: ArrayList<String>,
    ): CropState()

    object Error: CropState()
}