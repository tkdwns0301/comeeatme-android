package com.hand.comeeatme.view.main.home.newpost.album

import com.hand.comeeatme.data.Thumbnail
import com.hand.comeeatme.data.Thumbnail2

sealed class AlbumState {
    object UnInitialized : AlbumState()
    object Loading: AlbumState()

    data class Success(
        val photoList: List<Thumbnail2>
    ): AlbumState()

    data class Initialized(
        val thumbnailList: ArrayList<Thumbnail>,
    ) : AlbumState()

    object PhotoReady : AlbumState()

    object PhotoUnReady: AlbumState()

}