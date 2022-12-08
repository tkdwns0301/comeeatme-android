package com.hand.comeeatme.view.main.rank.restaurant

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.image.RestaurantImageContent
import com.hand.comeeatme.data.response.post.RestaurantPostContent
import com.hand.comeeatme.data.response.restaurant.DetailRestaurantData
import com.hand.comeeatme.databinding.FragmentDetailRestaurantBinding
import com.hand.comeeatme.util.widget.adapter.CustomBalloonAdapter
import com.hand.comeeatme.util.widget.adapter.RestaurantPostsAdapter
import com.hand.comeeatme.view.base.BaseFragment
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt


class DetailRestaurantFragment :
    BaseFragment<DetailRestaurantViewModel, FragmentDetailRestaurantBinding>() {
    companion object {
        const val TAG = "DetailRestaurantFragment"
        const val RESTAURANT_ID = "restaurantId"
        fun newInstance(restaurantId: Long): DetailRestaurantFragment {
            val bundle = bundleOf(
                RESTAURANT_ID to restaurantId
            )

            return DetailRestaurantFragment().apply {
                arguments = bundle
            }
        }
    }

    private val restaurantId by lazy {
        arguments?.getLong(RESTAURANT_ID, -1)
    }
    private lateinit var adapter: RestaurantPostsAdapter
    private lateinit var mapView: MapView

    override val viewModel by viewModel<DetailRestaurantViewModel>()
    override fun getViewBinding(): FragmentDetailRestaurantBinding =
        FragmentDetailRestaurantBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun observeData() {
        viewModel.detailRestaurantStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is DetailRestaurantState.Uninitialized -> {
                    viewModel.getDetailRestaurant(restaurantId!!)
                    viewModel.getRestaurantImage(restaurantId!!)
                    viewModel.getRestaurantPosts(restaurantId!!)
                }

                is DetailRestaurantState.Loading -> {

                }

                is DetailRestaurantState.Success -> {
                    setDetailView(it.response.data)
                    viewModel.getAddressToCoord(it.response.data.address.roadName)
                }

                is DetailRestaurantState.ImageSuccess -> {
                    setRestaurantImage(it.response.data.content)
                }

                is DetailRestaurantState.RestaurantPostsSuccess -> {
                    setAdapter(it.response.data.content)
                }

                is DetailRestaurantState.FavoriteSuccess -> {
                    val cnt = Integer.parseInt("${binding.tvFavoriteCnt.text}")
                    binding.tvFavoriteCnt.text = "${cnt+1}"
                }

                is DetailRestaurantState.UnFavoriteSuccess -> {
                    val cnt = Integer.parseInt("${binding.tvFavoriteCnt.text}")
                    binding.tvFavoriteCnt.text = "${cnt-1}"
                }

                is DetailRestaurantState.CoordSuccess -> {
                    viewModel.setLongitude(it.response.documents[0].x)
                    viewModel.setLatitude(it.response.documents[0].y)
                    setKakaoMap(it.response.documents[0].x, it.response.documents[0].y)
                }

                is DetailRestaurantState.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() = with(binding) {
        rvIncludingContent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        mapView = MapView(requireContext())
        KakaoMapView.addView(mapView)

        KakaoMapView.setOnTouchListener(OnTouchListener { view, motionEvent ->
            val action = motionEvent.action

            when (action) {
                MotionEvent.ACTION_DOWN -> slContent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP -> slContent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_MOVE -> slContent.requestDisallowInterceptTouchEvent(true)
            }
            false
        })

        toolbarRestaurantDetail.setNavigationOnClickListener {
            finish()
        }

        tbFavorite.setOnClickListener {
            if(tbFavorite.isChecked) {
                viewModel.favoriteRestaurant(restaurantId!!)
            } else {
                viewModel.unFavoriteRestaurant(restaurantId!!)
            }
        }

        srlRestaurantDetail.setOnRefreshListener {
            refresh()
        }

        clOpenKakaoMap.setOnClickListener {
            val url = "kakaomap://look?p=${viewModel.getLatitude()},${viewModel.getLongitude()}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        clAddress.setOnClickListener {
            val clipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Address", "${tvLocation.text}")

            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "클립보드에 주소를 복사하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setDetailView(data: DetailRestaurantData) = with(binding) {
        tvRestaurantName.text = data.name
        tvFavoriteCnt.text = "${data.favoriteCount}"
        tbFavorite.isChecked = data.favorited
        tvLocation.text = data.address.roadName

        flTag.removeAllViews()
        data.hashtags.forEach { hashTag ->
            flTag.addItem(hashTag)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setRestaurantImage(contents: List<RestaurantImageContent>) = with(binding) {
        if (contents.isEmpty()) {
            ivImage.setImageDrawable(requireContext().getDrawable(R.drawable.food1))
        } else {
            Glide.with(requireContext())
                .load(contents[0].imageUrl)
                .into(ivImage)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(contents: List<RestaurantPostContent>) {
        adapter = RestaurantPostsAdapter(
            requireContext(),
            contents
        )

        binding.rvIncludingContent.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setKakaoMap(longitude: String, latitude: String) {
        val mapPoint = MapPoint.mapPointWithGeoCoord(latitude.toDouble(), longitude.toDouble())
        mapView.setMapCenterPoint(mapPoint, true)
        mapView.setZoomLevel(1, true)

        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))

        val marker = MapPOIItem()
        marker.apply {
            itemName = "${binding.tvRestaurantName.text}"
            markerType = MapPOIItem.MarkerType.CustomImage
            customImageResourceId = R.drawable.marker_64
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage
            customSelectedImageResourceId = R.drawable.marker_64
            isCustomImageAutoscale = true
            setCustomImageAnchor(0.5f, 1.0f)
        }
        marker.mapPoint = mapPoint

        mapView.addPOIItem(marker)
    }


    @SuppressLint( "InflateParams", "SetTextI18n")
    private fun FlexboxLayout.addItem(tag: String) {

        val view = LayoutInflater.from(context).inflate(R.layout.layout_restaurant_detail_hashtag, null) as ConstraintLayout

        view.findViewById<TextView>(R.id.tv_HashTag).text = "#${viewModel.hashTagEngToKor[tag]}"
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )

        layoutParams.rightMargin = dpToPx(4)
        addView(view, childCount, layoutParams)
    }

    private fun dpToPx(dp: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
            .roundToInt()

    private fun refresh() {
        viewModel.getDetailRestaurant(restaurantId!!)
        viewModel.getRestaurantImage(restaurantId!!)
        viewModel.getRestaurantPosts(restaurantId!!)
        binding.srlRestaurantDetail.isRefreshing = false
    }

    private fun finish() {
        val manager: FragmentManager? = activity?.supportFragmentManager
        val ft: FragmentTransaction = manager!!.beginTransaction()

        val fragment = manager.findFragmentByTag(TAG)

        if (fragment != null) {
            ft.remove(fragment)
        }

        ft.commitAllowingStateLoss()
    }

}