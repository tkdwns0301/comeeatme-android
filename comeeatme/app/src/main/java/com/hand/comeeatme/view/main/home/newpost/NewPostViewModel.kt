package com.hand.comeeatme.view.main.home.newpost

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.home.PostRepository
import com.hand.comeeatme.data.repository.image.ImageRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.data.request.post.NewPostRequest
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class NewPostViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val postRepository: PostRepository,
    private val restaurantRepository: RestaurantRepository,
    private val imageRepository: ImageRepository,
) : BaseViewModel() {
    companion object {
        const val CHECKED_PHOTOS_KEY = "CheckedPhotos"
        const val COMPRESSED_PHOTO_KEY = "CompressedPhotos"
    }
    private val hashTagKorToEng = hashMapOf<String, String>(
        "감성있는" to "MOODY",
        "혼밥" to "EATING_ALON",
        "단체모임" to "GROUP_MEETING",
        "데이트" to "DATE",
        "특별한 날" to "SPECIAL_DAY",
        "신선한 재료" to "FRESH_INGREDIENT",
        "시그니쳐 메뉴" to "SIGNATURE_MENU",
        "가성비" to "COST_EFFECTIVENESS",
        "고급스러운" to "LUXURIOUSNESS",
        "자극적인" to "STRONG_TASTE",
        "친절" to "KINDNESS",
        "청결" to "CLEANLINESS",
        "주차장" to "PARKING",
        "반려동물 동반" to "PET",
        "아이 동반" to "CHILD",
        "24시간" to "AROUND_CLOCK"
    )

    private var checkedChipList: ArrayList<Chip> = arrayListOf()
    private var compressPhotoPathList: ArrayList<String> = arrayListOf()
    private var hashTagList: ArrayList<String> = arrayListOf()
    private var restaurantId: Long? = null
    private var content: String? = null

    val newPostStateLiveData = MutableLiveData<NewPostState>(NewPostState.Uninitialized)

    fun addHashTag(tag: String, chip: Chip) {
        hashTagList.add(hashTagKorToEng[tag]!!)
        checkedChipList.add(chip)
    }

    fun removeHashTag(tag: String, chip: Chip) {
        hashTagList.remove(hashTagKorToEng[tag]!!)
        checkedChipList.remove(chip)
    }

    fun getCheckedChipList(): List<Chip> {
        return checkedChipList
    }

    fun clearHashTag() {
        for (i in 0 until checkedChipList.size) {
            checkedChipList[0].isChecked = false
        }
    }

    fun setRestaurantId(restaurantId: Long) {
        this.restaurantId = restaurantId

        if (isReady()) {
            newPostStateLiveData.value = NewPostState.NewPostReady
        } else {
            newPostStateLiveData.value = NewPostState.NewPostUnReady
        }
    }

    private fun isReady(): Boolean {
        return restaurantId != null && hashTagList.isNotEmpty() && compressPhotoPathList.isNotEmpty() && content != null
    }


    fun setResultPhotoList(compressPhotos: ArrayList<String>) = viewModelScope.launch {
        compressPhotoPathList = compressPhotos

        newPostStateLiveData.value = NewPostState.CompressPhotoFinish(
            compressPhotoList = compressPhotos
        )
    }

    fun setContent(content: String) {
        this.content = content

        if (isReady()) {
            newPostStateLiveData.value = NewPostState.NewPostReady
        } else {
            newPostStateLiveData.value = NewPostState.NewPostUnReady
        }
    }

    fun removeContent() {
        this.content = null
        newPostStateLiveData.value = NewPostState.NewPostUnReady
    }


    fun searchRestaurants(
        page: Long?,
        size: Long?,
        sort: Boolean?,
        name: String,
    ) = viewModelScope.launch {
        newPostStateLiveData.value = NewPostState.Loading
        val response =
            restaurantRepository.getSearchRestaurants("${appPreferenceManager.getAccessToken()}",
                page, size, sort, name
            )

        response?.let {
            newPostStateLiveData.value = NewPostState.SearchRestaurantSuccess(
                restaurants = it
            )
        } ?: run {
            newPostStateLiveData.value = NewPostState.Error(
                "검색한 단어의 식당을 찾을 수 없었습니다."
            )
        }

    }

    fun getImageIds() = viewModelScope.launch {
        newPostStateLiveData.value = NewPostState.Loading

        val images: ArrayList<MultipartBody.Part> = arrayListOf()

        compressPhotoPathList.forEachIndexed { index, path ->
            val file = File(path)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            images.add(MultipartBody.Part.createFormData("images", "test-image$index.webp", requestFile))
        }

        Log.e("images", "$images")

        val response = imageRepository.sendImages(
            "${appPreferenceManager.getAccessToken()}",
            images
        )

        response?.let {
            sendNewPost(it.data.ids)
        } ?:run {
            newPostStateLiveData.value = NewPostState.Error("이미지를 전송하기에 실패했습니다.")
        }
    }

    private fun sendNewPost(imageIds: List<Long>) = viewModelScope.launch {
        val newPostRequest = NewPostRequest(restaurantId, hashTagList, imageIds, content)
        val response =
            postRepository.putNewPost("${appPreferenceManager.getAccessToken()}", newPostRequest)

        response?.let {
            newPostStateLiveData.value = NewPostState.Success(
                response = it
            )
        } ?: run {
            newPostStateLiveData.value = NewPostState.Error(
                "새로운 글 쓰기 실패"
            )
        }
    }
}