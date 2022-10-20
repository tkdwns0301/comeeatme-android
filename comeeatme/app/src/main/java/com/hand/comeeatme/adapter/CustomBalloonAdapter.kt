package com.hand.comeeatme.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.hand.comeeatme.R
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem

class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
    val callOutBalloon: View = inflater.inflate(R.layout.layout_marker_balloon, null)
    val name : TextView = callOutBalloon.findViewById(R.id.tv_Name)

    override fun getCalloutBalloon(p0: MapPOIItem?): View {
        name.text = p0!!.itemName
        return callOutBalloon
    }

    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View {
        return callOutBalloon
    }

}