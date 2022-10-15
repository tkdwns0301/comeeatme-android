package com.hand.comeeatme.view.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.FragmentMapBinding
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment: Fragment(R.layout.fragment_map) {
    private var _binding : FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapView = MapView(activity)

        binding.rlMap.addView(mapView)

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.54892296550104, 126.99089033876304), true)
        mapView.setZoomLevel(4, true)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}