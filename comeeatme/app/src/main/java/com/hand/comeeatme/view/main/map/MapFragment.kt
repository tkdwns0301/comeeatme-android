package com.hand.comeeatme.view.main.map

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hand.comeeatme.R
import com.hand.comeeatme.adapter.CustomBalloonAdapter
import com.hand.comeeatme.databinding.FragmentMapBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint

class MapFragment: Fragment(R.layout.fragment_map) {
    private var _binding : FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var isTracking = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    @SuppressWarnings("MissingPermission")
    private fun initView() {
        val lm: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val uLat = userNowLocation?.latitude
        val uLong = userNowLocation?.longitude

        binding.mvMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(uLat!!, uLong!!), true)
        binding.mvMap.setZoomLevel(1, true)

        binding.ibCurrentLocation.setOnClickListener {
            val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationPermissionCheck()
            }
        }

    }

    private fun locationPermissionCheck() {
        val preference = requireActivity().getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)

        if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("현재 위치를 확인하시려면 위치 권환을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                if(isFirstCheck) {
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
                } else {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("현재 위치를 확인하시려면, 설정에서 위치 권환을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.hand.comeeatme"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {
//            if(isTracking) {
//                isTracking = false
//                binding.mvMap.setCustomCurrentLocationMarkerImage(R.drawable.tracking3, MapPOIItem.ImageOffset(16,16))
//                binding.mvMap.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
//            } else {
//                isTracking = true
//                val lm: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//
//                val uLat = userNowLocation?.latitude
//                val uLong = userNowLocation?.longitude
//
//                binding.mvMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(uLat!!, uLong!!), true)
//                binding.mvMap.setZoomLevel(1, true)
//                binding.mvMap.setCustomCurrentLocationMarkerTrackingImage(R.drawable.traking2, MapPOIItem.ImageOffset(16, 16))
//                binding.mvMap.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
//            }
            findCurrentLocation()
        }
    }

    private fun startTracking() {

    }


    @SuppressWarnings("MissingPermission")
    private fun findCurrentLocation() {
        val lm: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val uLat = userNowLocation?.latitude
        val uLong = userNowLocation?.longitude
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLat!!, uLong!!)

        val marker = MapPOIItem()

        binding.mvMap.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))

        marker.apply {
            itemName="현재위치"
            mapPoint = uNowPosition
            markerType = MapPOIItem.MarkerType.CustomImage
            customImageResourceId = R.drawable.marker
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage
            customSelectedImageResourceId = R.drawable.marker
            isCustomImageAutoscale = true
            setCustomImageAnchor(0.5f, 1f)
            isShowDisclosureButtonOnCalloutBalloon = true
        }


        binding.mvMap.addPOIItem(marker)
        binding.mvMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(uLat!!, uLong!!), true)
        binding.mvMap.setZoomLevel(1, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}