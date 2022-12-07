package com.hand.comeeatme.view.main.rank

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.hand.comeeatme.data.response.restaurant.RestaurantsRankContent
import com.hand.comeeatme.databinding.FragmentRankBinding
import com.hand.comeeatme.util.widget.adapter.rank.RestaurantsRankAdapter
import com.hand.comeeatme.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class RankFragment : BaseFragment<RankViewModel, FragmentRankBinding>() {
    companion object {
        const val TAG = "RankFragment"
        fun newInstance() = RankFragment()
    }

    override val viewModel by viewModel<RankViewModel>()
    override fun getViewBinding(): FragmentRankBinding = FragmentRankBinding.inflate(layoutInflater)

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    private lateinit var adapter: RestaurantsRankAdapter

    override fun observeData() {
        viewModel.rankStateLiveDate.observe(viewLifecycleOwner) {
            when (it) {
                is RankState.Uninitialized -> {
                    if (checkPermissionForLocation(requireContext())) {
                        getCurrentLocation()
                    }
                }

                is RankState.Loading -> {

                }

                is RankState.Success -> {
                    setAdapter(it.response.data.content)
                }

                is RankState.CurrentAddressSuccess -> {
                    val address = it.depth1 + " " + it.depth2
                    binding.tvLocation.text = address

                    viewModel.getRestaurantsRank(0, 10, it.addressCode, 1, "postCount,desc")
                }

                is RankState.Error -> {
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initView() = with(binding) {
        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        srlRank.setOnRefreshListener {
            refresh()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<RestaurantsRankContent>) {
        adapter = RestaurantsRankAdapter(
            requireContext(),
            contents,
            favoriteRestaurant = {
                viewModel.favoriteRestaurants(it)
            },
            unFavoriteRestaurant = {
                viewModel.unFavoriteRestuarants(it)
            },
            viewModel.getDepth1(),
            viewModel.getDepth2(),
        )

        binding.rvRank.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun refresh() {
        if(checkPermissionForLocation(requireContext())) {
            getCurrentLocation()
        }

        binding.srlRank.isRefreshing = false
    }


    private fun getCurrentLocation() {
//FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest,
            mLocationCallback,
            Looper.myLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation!!)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location

        viewModel.getAddress("${mLastLocation.latitude}", "${mLastLocation.longitude}")
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkPermissionForLocation(context: Context): Boolean {
// Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(requireContext(), "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}