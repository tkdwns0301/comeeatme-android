package com.hand.comeeatme

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView


@SuppressLint("AppCompatCustomView")
class CustomTextOutLineView : TextView {
    // 클래스명 다르게 생성시 붙여넣으면 이곳을 클래스명에 맞게 수정
    private var stroke = false
    private var strokeWidth = 0.0f
    private var strokeColor = 0

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context,
        attrs,
        defStyle) {
// 클래스명 다르게 생성시 붙여넣으면 이곳을 클래스명에 맞게 수정
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
// 클래스명 다르게 생성시 붙여넣으면 이곳을 클래스명에 맞게 수정
        initView(context, attrs)
    }

    constructor(context: Context?) : super(context) {
// 클래스명 다르게 생성시 붙여넣으면 이곳을 클래스명에 맞게 수정
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        // 이부분에서 오류가 발생할 경우 attrs.xml을 만들지 않아서니 넘어가도록합니다.
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextOutLineView)
        // 클래스명 다르게 생성시 붙여넣으면 이곳을 클래스명에 맞게 수정
        stroke = a.getBoolean(R.styleable.CustomTextOutLineView_textStroke, false)
        // 클래스명 다르게 생성시 붙여넣으면 이곳을 클래스명에 맞게 수정
        strokeWidth = a.getFloat(R.styleable.CustomTextOutLineView_textStrokeWidth, 0.0f)
        // 클래스명 다르게 생성시 붙여넣으면 이곳을 클래스명에 맞게 수정
        strokeColor = a.getColor(R.styleable.CustomTextOutLineView_textStrokeColor, -0x1)
        // 클래스명 다르게 생성시 붙여넣으면 이곳을 클래스명에 맞게 수정
    }

    override fun onDraw(canvas: Canvas?) {
        if (stroke) {
            val states = textColors
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            setTextColor(strokeColor)
            super.onDraw(canvas)
            paint.style = Paint.Style.FILL
            setTextColor(states)
        }
        super.onDraw(canvas)
    }
}