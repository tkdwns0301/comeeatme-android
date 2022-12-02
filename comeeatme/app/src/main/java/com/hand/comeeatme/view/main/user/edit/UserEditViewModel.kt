package com.hand.comeeatme.view.main.user.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.image.ImageRepository
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.request.member.MemberModifyRequest
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserEditViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val memberRepository: MemberRepository,
    private val imageRepository: ImageRepository,
) : BaseViewModel() {
    val userEditStateLiveData = MutableLiveData<UserEditState>(UserEditState.Uninitialized)

    private var compressPhotoPathList: ArrayList<String> = arrayListOf()

    fun getCompressPhotoPathList(): ArrayList<String> {
        return compressPhotoPathList
    }

    fun setResultPhotoList(compressPhotos: ArrayList<String>) = viewModelScope.launch {
        compressPhotoPathList = compressPhotos

        userEditStateLiveData.value = UserEditState.CompressPhotoFinish(
            compressPhotoList = compressPhotos
        )
    }

    fun getImageIds(nickname: String, introduction: String?) = viewModelScope.launch {
        userEditStateLiveData.value = UserEditState.Loading

        val images: ArrayList<MultipartBody.Part> = arrayListOf()

        compressPhotoPathList.forEachIndexed { index, path ->
            val file = File(path)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            images.add(MultipartBody.Part.createFormData("images",
                "test-image$index.webp",
                requestFile))
        }

        val response =
            imageRepository.sendImages("${appPreferenceManager.getAccessToken()}", images)

        response?.let {
            modifyMemberInformation(nickname, introduction, it.data.ids[0])
        } ?: run {
            userEditStateLiveData.value = UserEditState.Error(
                "프로필 사진을 전송하는 도중 오류가 발생했습니다."
            )
        }
    }



    private fun modifyMemberInformation(nickname: String, introduction: String?, imageId: Long?) =
        viewModelScope.launch {
            val information = MemberModifyRequest(nickname, introduction, imageId)

            val response = memberRepository.modifyMemberInformation(
                "${appPreferenceManager.getAccessToken()}",
                information
            )

            response?.let {
                appPreferenceManager.putMemberId(it.data.id)
                userEditStateLiveData.value = UserEditState.Success
            } ?: run {
                userEditStateLiveData.value = UserEditState.Error(
                    "프로필을 수정하는 도중 오류가 발생했습니다."
                )
            }
        }
}