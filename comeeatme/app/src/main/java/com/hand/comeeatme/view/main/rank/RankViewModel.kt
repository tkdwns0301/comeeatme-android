package com.hand.comeeatme.view.main.rank

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hand.comeeatme.BuildConfig
import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.code.CodeRepository
import com.hand.comeeatme.data.repository.favorite.FavoriteRepository
import com.hand.comeeatme.data.repository.kakao.KakaoRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.view.base.BaseViewModel
import kotlinx.coroutines.launch

class RankViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val kakaoRepository: KakaoRepository,
    private val restaurantRepository: RestaurantRepository,
    private val favoriteRepository: FavoriteRepository,
    private val codeRepository: CodeRepository,
) : BaseViewModel() {
    val rankStateLiveDate = MutableLiveData<RankState>(RankState.Uninitialized)

    private var depth1: String? = null
    private var depth2: String? = null
    private var addCode: String? = null

    fun getDepth1(): String = depth1!!
    fun getDepth2(): String = depth2!!
    fun getAddCode(): String = addCode!!

    fun setDepth1Depth2AddressCode(depth1: String, depth2: String, addCode: String) {
        this.depth1 = depth1
        this.depth2 = depth2
        this.addCode = addCode

        rankStateLiveDate.value = RankState.CurrentAddressSuccess(
            depth1 = depth1,
            depth2 = depth2,
            addressCode =  addCode
        )
    }

    fun getAddress(latitude: String, longitude: String) = viewModelScope.launch {
        rankStateLiveDate.value = RankState.Loading

        val response = kakaoRepository.getAddress(
            BuildConfig.kakao_rest_api_key,
            longitude,
            latitude,
        )

        response?.let {
            var addressCode = it.documents[0].code
            addressCode = addressCode.substring(0, 5) + "00000"

            depth1 = it.documents[0].region_1depth_name
            depth2 = it.documents[0].region_2depth_name
            addCode = addressCode

            rankStateLiveDate.value = RankState.CurrentAddressSuccess(
                depth1 = depth1!!,
                depth2 = depth2!!,
                addressCode = addressCode
            )
        } ?: run {

        }
    }

    fun getRestaurantsRank(
        page: Long?,
        size: Long?,
        addressCode: String,
        perImageNum: Long,
        sort: String,
    ) = viewModelScope.launch {
        rankStateLiveDate.value = RankState.Loading

        val response = restaurantRepository.getRestaurantsRank(
            "${appPreferenceManager.getAccessToken()}",
            page,
            size,
            addressCode,
            perImageNum,
            sort
        )

        response?.let {
            rankStateLiveDate.value = RankState.Success(
                response = it
            )
        } ?: run {
            rankStateLiveDate.value = RankState.Error(
                "랭킹을 불러오는 도중 오류가 발생했습니다."
            )
        }
    }

    fun favoriteRestaurants(restaurantId: Long) = viewModelScope.launch {
        val response = favoriteRepository.favoriteRestaurant(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {

        }?:run {
            "즐겨찾기를 하는 도중 오류가 발생했습니다."
        }
    }

    fun unFavoriteRestaurants(restaurantId: Long) = viewModelScope.launch {
        val response = favoriteRepository.unFavoriteRestaurant(
            "${appPreferenceManager.getAccessToken()}",
            restaurantId
        )

        response?.let {

        }?:run {
            "즐겨찾기를 취소하는 도중 오류가 발생했습니다."
        }
    }

    fun getAddressCode(parentCode: String) = viewModelScope.launch {
        val response = codeRepository.getAddressCode(
            "${appPreferenceManager.getAccessToken()}",
            parentCode = parentCode
        )

        response?.let {
            rankStateLiveDate.value = RankState.AddressCodeSuccess(
                response = it
            )
        }
    }
}