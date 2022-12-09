package com.hand.comeeatme.view.main.rank.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.BuildConfig
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.favorite.FavoriteRepository
import com.hand.comeeatme.data.repository.image.ImageRepository
import com.hand.comeeatme.data.repository.kakao.KakaoRepository
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class DetailRestaurantViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val restaurantRepository: RestaurantRepository,
    private val imageRepository: ImageRepository,
    private val favoriteRepository: FavoriteRepository,
    private val postRepository: PostRepository,
    private val kakaoRepository: KakaoRepository,
) : BaseViewModel() {

    val detailRestaurantStateLiveData =
        MutableLiveData<DetailRestaurantState>(DetailRestaurantState.Uninitialized)

    val hashTagEngToKor = hashMapOf<String, String>(
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

    private var longitude: String? = null
    private var latitude: String? = null

    fun setLongitude(long: String) {
        longitude = long
    }

    fun getLongitude(): String = longitude!!
    fun setLatitude(lat: String) {
        latitude = lat
    }

    fun getLatitude(): String = latitude!!


    fun hashTagEngToKor(hashTag: String): String {
        return hashTagEngToKor[hashTag]!!
    }

    fun getRestaurantImage(restaurantId: Long) = viewModelScope.launch {
        detailRestaurantStateLiveData.value = DetailRestaurantState.Loading

        val response = imageRepository.getRestaurantImage(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {
            detailRestaurantStateLiveData.value = DetailRestaurantState.ImageSuccess(
                response = it
            )
        } ?: run {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Error(
                "음식점 사진을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun getDetailRestaurant(restaurantId: Long) = viewModelScope.launch {
        detailRestaurantStateLiveData.value = DetailRestaurantState.Loading

        val response = restaurantRepository.getDetailRestaurant(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Success(
                response = it
            )
        } ?: run {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Error(
                "음식점 정보를 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun favoriteRestaurant(restaurantId: Long) = viewModelScope.launch {
        val response = favoriteRepository.favoriteRestaurant(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {
            detailRestaurantStateLiveData.value = DetailRestaurantState.FavoriteSuccess
        } ?: run {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Error(
                "즐겨찾기를 하는 도중 오류가 발생했습니다."
            )
        }
    }

    fun unFavoriteRestaurant(restaurantId: Long) = viewModelScope.launch {
        val response = favoriteRepository.unFavoriteRestaurant(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )
        response?.let {
            detailRestaurantStateLiveData.value = DetailRestaurantState.UnFavoriteSuccess
        } ?: run {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Error(
                "즐겨찾기를 취소하는 도중 오류가 발생했습니다."
            )
        }
    }

    fun getRestaurantPosts(restaurantId: Long) = viewModelScope.launch {
        detailRestaurantStateLiveData.value = DetailRestaurantState.Loading

        val response = postRepository.getRestaurantPosts(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {
            detailRestaurantStateLiveData.value = DetailRestaurantState.RestaurantPostsSuccess(
                response = it
            )
        } ?: run {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Error(
                "음식점과 관련된 게시글을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun getAddressToCoord(query: String) = viewModelScope.launch {
        val response = kakaoRepository.getAddressToCoord(
            BuildConfig.kakao_rest_api_key,
            query
        )

        response?.let {
            detailRestaurantStateLiveData.value = DetailRestaurantState.CoordSuccess(
                response = it
            )
        } ?: run {
            detailRestaurantStateLiveData.value = DetailRestaurantState.Error(
                "지도를 업데이트 하는 도중 오류가 발생했습니다."
            )
        }
    }

}