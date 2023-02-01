package com.hand.comeeatme.data

import android.graphics.Bitmap

class ImageSharedPreferences {
    var checkedImageMap = LinkedHashMap<Int, Bitmap>()


    fun getMap() : LinkedHashMap<Int, Bitmap> {
        return this.checkedImageMap
    }

    fun addMap(key: Int, value: Bitmap) {
        this.checkedImageMap[key] = value
    }

    fun setMap(map: LinkedHashMap<Int, Bitmap>) {
        this.checkedImageMap = map
    }
}