package com.hand.comeeatme.view.main.home.newpost.album

import com.hand.comeeatme.data.Thumbnail

sealed class AlbumState {
    object UnInitialized : AlbumState()

    data class Initialized(
        val thumbnailList: ArrayList<Thumbnail>,
    ) : AlbumState()

    object PhotoReady : AlbumState()

    object PhotoUnReady: AlbumState()

}