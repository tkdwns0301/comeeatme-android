package com.hand.comeeatme.view.main.home.newpost.album

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.hand.comeeatme.data.Thumbnail2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class GalleryPhotoRepository(
    private val context: Context
) {

    suspend fun getAllPhotos(): MutableList<Thumbnail2> = withContext(Dispatchers.IO) {
        val galleryPhotoModelList = mutableListOf<Thumbnail2>()
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val query: Cursor?
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
        )
        val resolver = context.contentResolver
        query = resolver?.query(
            uriExternal,
            projection,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_ADDED} DESC"
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)

            var index = 0

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))).toString()
                val contentUri = ContentUris.withAppendedId(uriExternal, id)

                galleryPhotoModelList.add(
                    Thumbnail2(
                        id = id,
                        position = index++,
                        uri = contentUri,
                        path = path,
                    )
                )
            }
        }

        galleryPhotoModelList
    }
}