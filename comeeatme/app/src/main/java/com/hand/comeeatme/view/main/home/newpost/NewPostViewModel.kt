package com.hand.comeeatme.view.main.home.newpost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.image.ImageRepository
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.data.request.post.ModifyPostRequest
import com.hand.comeeatme.data.request.post.NewPostRequest
import com.hand.comeeatme.data.response.restaurant.SimpleRestaurantContent
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

    private val hashTagEngToKor = hashMapOf<String, String>(
        "MOODY" to "감성있는",
        "EATING_ALON" to "혼밥",
        "GROUP_MEETING" to "단체모임",
        "DATE" to "데이트",
        "SPECIAL_DAY" to "특별한 날",
        "FRESH_INGREDIENT" to "신선한 재료",
        "SIGNATURE_MENU" to "시그니쳐 메뉴",
        "COST_EFFECTIVENESS" to "가성비",
        "LUXURIOUSNESS" to "고급스러운",
        "STRONG_TASTE" to "자극적인",
        "KINDNESS" to "친절",
        "CLEANLINESS" to "청결",
        "PARKING" to "주차장",
        "PET" to "반려동물 동반",
        "CHILD" to "아이 동반",
        "AROUND_CLOCK" to "24시간"
    )

    private var chipList: ArrayList<Chip> = arrayListOf()
    private var checkedChipList: ArrayList<Chip> = arrayListOf()
    private var compressImagePathList: ArrayList<String> = arrayListOf()
    //private var selectedImageList: ArrayList<String> = arrayListOf()
    private var hashTagList: ArrayList<String> = arrayListOf()
    private var restaurantId: Long? = null
    private var content: String? = null

    private var page: Long = 0
    private var contents = arrayListOf<SimpleRestaurantContent>()
    private var isLast: Boolean = false
    private var query: String = ""

    val newPostStateLiveData = MutableLiveData<NewPostState>(NewPostState.Uninitialized)

    fun setQuery(query: String) {
        this.query = query
    }
    fun getIsLast(): Boolean = isLast
    fun setIsLast(isLast: Boolean) {
        this.isLast = isLast
    }


//    fun getHashTags() : ArrayList<String> = hashTagList
//
//    fun clearSelectedImageList() {
//        this.selectedImageList.clear()
//    }
//
//    fun addSelectedImageList(path: String) {
//        this.selectedImageList.add(path)
//    }
//
//    fun getSelectedImageList(): ArrayList<String> {
//        return this.selectedImageList
//    }

    fun addChip(chip: Chip) {
        chipList.add(chip)
    }

    fun getChipList(): List<Chip> = chipList

    fun addHashTag(tag: String, chip: Chip) {
        hashTagList.add(hashTagKorToEng[tag]!!)
        checkedChipList.add(chip)

        if (isReady()) {
            newPostStateLiveData.value = NewPostState.NewPostReady
        } else {
            newPostStateLiveData.value = NewPostState.NewPostUnReady
        }
    }

    fun removeHashTag(tag: String, chip: Chip) {
        hashTagList.remove(hashTagKorToEng[tag]!!)
        checkedChipList.remove(chip)

        if (isReady()) {
            newPostStateLiveData.value = NewPostState.NewPostReady
        } else {
            newPostStateLiveData.value = NewPostState.NewPostUnReady
        }
    }

    fun hashTagKorToEng(tag: String): String {
        return this.hashTagKorToEng[tag]!!
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
        return restaurantId != null && hashTagList.isNotEmpty() && compressImagePathList.isNotEmpty() && content != null
    }


    fun setResultPhotoList(compressImages: ArrayList<String>) = viewModelScope.launch {
        compressImagePathList = compressImages

        newPostStateLiveData.value = NewPostState.CompressPhotoFinish(
            compressPhotoList = compressImages
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
        isRefresh: Boolean,
    ) = viewModelScope.launch {

        if(isRefresh) {
            page = 0
            contents = arrayListOf()
        }

        val response =
            restaurantRepository.getSearchRestaurants("${appPreferenceManager.getAccessToken()}",
                page++, 10, query
            )

        response?.let {
            if (it.data.content.isNotEmpty()) {
                contents.addAll(it.data.content)

                newPostStateLiveData.value = NewPostState.SearchRestaurantSuccess(
                    restaurants = contents
                )
                isLast = false
            } else {
                isLast = true

                newPostStateLiveData.value = NewPostState.SearchRestaurantSuccess(
                    restaurants = contents
                )
            }
        } ?: run {
            newPostStateLiveData.value = NewPostState.Error(
                "검색한 단어의 식당을 찾을 수 없었습니다."
            )
        }

    }

    fun getImageIds() = viewModelScope.launch {
        newPostStateLiveData.value = NewPostState.Loading

        val images: ArrayList<MultipartBody.Part> = arrayListOf()

        compressImagePathList.forEachIndexed { index, path ->
            val file = File(path)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            images.add(MultipartBody.Part.createFormData("images",
                "test-image$index.webp",
                requestFile))
        }

        val response = imageRepository.sendImages(
            "${appPreferenceManager.getAccessToken()}",
            images
        )

        response?.let {
            sendNewPost(it.data.ids)
        } ?: run {
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

    fun getDetailPost(postId: Long) = viewModelScope.launch {
        newPostStateLiveData.value = NewPostState.Loading

        val response =
            postRepository.getDetailPost("${appPreferenceManager.getAccessToken()}", postId)

        response?.let {
            newPostStateLiveData.value = NewPostState.DetailPostSuccess(
                response = it
            )
        } ?: run {
            newPostStateLiveData.value = NewPostState.Error(
                "수정을 위한 글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun modifyPost(postId: Long) = viewModelScope.launch {
        newPostStateLiveData.value = NewPostState.Loading

        val modifyPostRequest = ModifyPostRequest(restaurantId!!, hashTagList, content!!)

        val response = postRepository.modifyPost("${appPreferenceManager.getAccessToken()}",
            postId,
            modifyPostRequest)

        response?.let {
            newPostStateLiveData.value = NewPostState.ModifyPostSuccess
        } ?: run {
            newPostStateLiveData.value = NewPostState.Error(
                "글을 수정하는 도중 오류가 발생했습니다."
            )
        }
    }
}