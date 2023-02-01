package com.hand.comeeatme.view.main.rank

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.restaurant.RestaurantsRankContent
import com.hand.comeeatme.databinding.FragmentRankBinding
import com.hand.comeeatme.util.widget.adapter.rank.RestaurantsRankAdapter
import com.hand.comeeatme.view.base.BaseFragment
import com.hand.comeeatme.view.dialog.RankSortDialog
import com.hand.comeeatme.view.main.home.search.SearchFragment
import com.hand.comeeatme.view.main.rank.region.RegionActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


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

    @SuppressLint("SetTextI18n")
    override fun observeData() {
        viewModel.rankStateLiveDate.observe(viewLifecycleOwner) {
            when (it) {
                is RankState.Uninitialized -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    if(checkPermissionForLocation(requireContext())) {
                        getCurrentLoc()
                    }

                }

                is RankState.Loading -> {
                    binding.clLoading.isVisible = true
                    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is RankState.Success -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    setAdapter(it.response)
                }

                is RankState.CurrentAddressSuccess -> {
                    binding.tvDepth1.text = it.depth1
                    binding.tvDepth2.text = "${it.depth2} "

                    viewModel.getRestaurantsRank(true, it.addressCode, 1)
                }

                is RankState.Error -> {
                    binding.clLoading.isGone = true
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initView() = with(binding) {
        Glide.with(requireContext())
            .load(R.drawable.loading)
            .into(ivLoading)

        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        srlRank.setOnRefreshListener {
            refresh()
        }

        clLocation.setOnClickListener {
            startActivityForResult(RegionActivity.newIntent(requireContext(),
                viewModel.getDepth1(),
                viewModel.getDepth2(),
                viewModel.getAddCode()), 100)
        }

        ibSearch.setOnClickListener {
            val manager: FragmentManager =
                (requireContext() as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, SearchFragment.newInstance(), SearchFragment.TAG)
            ft.addToBackStack(SearchFragment.TAG)
            ft.commitAllowingStateLoss()
        }

        clSort.setOnClickListener {
            RankSortDialog(
                requireContext(),
                postSort = {
                    viewModel.setSort(it)
                    viewModel.getRestaurantsRank(true,  viewModel.getAddCode(), 1)
                    tvSort.text = "게시글순 "
                },
                favoriteSort = {
                    viewModel.setSort(it)
                    viewModel.getRestaurantsRank(true , viewModel.getAddCode(), 1)
                    tvSort.text = "즐겨찾기순 "
                }
            ).show()
        }

        rvRank.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!viewModel.getIsLast()) {
                    if(rvRank.canScrollVertically(1)) {
                        viewModel.setIsLast(true)
                        viewModel.getRestaurantsRank(false, viewModel.getAddCode(), 1)
                    }
                }
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<RestaurantsRankContent>) {
        if(contents.isNotEmpty()) {
            binding.rvRank.isVisible = true
            binding.clNonPost.isGone = true

            val recyclerViewState = binding.rvRank.layoutManager?.onSaveInstanceState()

            adapter = RestaurantsRankAdapter(
                requireContext(),
                contents,
                favoriteRestaurant = {
                    viewModel.favoriteRestaurants(it)
                },
                unFavoriteRestaurant = {
                    viewModel.unFavoriteRestaurants(it)
                },
                viewModel.getDepth1(),
                viewModel.getDepth2(),
            )

            binding.rvRank.adapter = adapter
            binding.rvRank.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        } else {
            binding.rvRank.isGone = true
            binding.clNonPost.isVisible = true
        }


    }

//    @SuppressLint("NotifyDataSetChanged")
//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if(!hidden) {
//            adapter.notifyDataSetChanged()
//        }
//    }

    private fun refresh() {
        if(checkPermissionForLocation(requireContext())) {
            getCurrentLoc()
        }

        binding.srlRank.isRefreshing = false
    }

    private fun getCurrentLoc() {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        val userLocation: Location = getLatLng(locationManager)

        val latitude = userLocation.latitude
        val longitude = userLocation.longitude
        Log.d("CheckCurrentLocation", "현재 내 위치 값: $latitude, $longitude")

        viewModel.getAddress("$latitude", "$longitude")

    }

    private fun getLatLng(locationManager: LocationManager?) : Location {
        var currentLatLng: Location? = null

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
            getLatLng(locationManager)
        } else {
            val locationProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locationManager?.getLastKnownLocation(locationProvider)
        }
        return currentLatLng!!
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
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
                getCurrentLoc()
            } else {
                Toast.makeText(requireContext(), "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 100) {
            viewModel.setDepth1Depth2AddressCode(data!!.getStringExtra(RegionActivity.DEPTH1) as String,
                data!!.getStringExtra(RegionActivity.DEPTH2) as String,
                data!!.getStringExtra(RegionActivity.ADDRESS_CODE) as String)
        }
    }

}