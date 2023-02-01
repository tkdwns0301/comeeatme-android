package com.hand.comeeatme.view.main.home.hashtag

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.ActivityHashtagBinding
import com.hand.comeeatme.view.main.home.HomeFragment
import kotlin.math.roundToInt

class HashTagActivity : AppCompatActivity() {
    companion object {
        const val CHECKED_HASHTAG = "checkedHashTagList"

        fun newIntent(context: Context, checkedChipList: ArrayList<String>) =
            Intent(context, HashTagActivity::class.java)
                .putExtra(CHECKED_HASHTAG, checkedChipList)
    }

    private var _binding: ActivityHashtagBinding? = null
    private val binding get() = _binding!!

    private val moodList: Array<String> = arrayOf("혼밥", "데이트", "단체모임", "특별한 날", "감성있는")
    private val priceList: Array<String> = arrayOf("가성비", "자극적인", "고급스러운", "신선한 재료", "시그니쳐 메뉴")
    private val etcList: Array<String> = arrayOf("친절", "청결", "반려동물 동반", "아이 동반", "24시간", "주차장")
    private var checkedChipList = ArrayList<Chip>()
    private var checkedHashTagList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHashtagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkedHashTagList = intent.getStringArrayListExtra(CHECKED_HASHTAG) as ArrayList<String>

        initView()
        initListener()
    }

    private fun initView() {
        moodList.forEach {
            if (checkedHashTagList!!.contains(it)) {
                binding.flMood.addItem(it, true)
            } else {
                binding.flMood.addItem(it, false)
            }

        }

        priceList.forEach {
            if (checkedHashTagList!!.contains(it)) {
                binding.flPrice.addItem(it, true)
            } else {
                binding.flPrice.addItem(it, false)
            }
        }

        etcList.forEach {
            if (checkedHashTagList!!.contains(it)) {
                binding.flEtc.addItem(it, true)
            } else {
                binding.flEtc.addItem(it, false)
            }
        }


    }


    private fun initListener() = with(binding) {
        ibPrev.setOnClickListener {
            // TODO 해쉬태그 검색 결과 전송
            val intent = Intent(applicationContext, HomeFragment::class.java)
            intent.putExtra(CHECKED_HASHTAG, checkedHashTagList)
            setResult(RESULT_OK, intent)
            finish()
        }

        tvFinish.setOnClickListener {
            val intent = Intent(applicationContext, HomeFragment::class.java)
            intent.putExtra(CHECKED_HASHTAG, checkedHashTagList)
            setResult(RESULT_OK, intent)
            finish()
        }

        tvInit.setOnClickListener {

            for (i in 0 until checkedChipList.size) {
                checkedChipList[0].isChecked = false
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun FlexboxLayout.addItem(tag: String, isSelected: Boolean) {

        val chip = LayoutInflater.from(context).inflate(R.layout.layout_chip_custom, null) as Chip

        chip.apply {
            text = "  $tag  "
            textSize = 14f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            isCheckable = true
            checkedIcon = null

            val nonClickBackground = ContextCompat.getColor(context, R.color.white)
            val clickBackground = ContextCompat.getColor(context, R.color.basic)

            chipBackgroundColor = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ),
                intArrayOf(nonClickBackground, clickBackground)
            )

            val nonClickStroke = ContextCompat.getColor(context, R.color.basic68)
            val clickStroke = ContextCompat.getColor(context, R.color.transparent)

            chipStrokeColor = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ),
                intArrayOf(nonClickStroke, clickStroke)
            )

            val nonCLickTextColor = ContextCompat.getColor(context, R.color.nonCheck)
            val clickTextColor = ContextCompat.getColor(context, R.color.white)
            //텍스트
            setTextColor(
                ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(nonCLickTextColor, clickTextColor)
                )

            )

            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!checkedHashTagList!!.contains(tag)) {
                        checkedHashTagList!!.add(tag)
                        checkedChipList.add(chip)
                    }
                } else {
                    checkedChipList.remove(chip)
                    checkedHashTagList!!.remove(tag)
                }
            }

        }

        chip.isChecked = isSelected

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )

        layoutParams.rightMargin = dpToPx(10)
        addView(chip, childCount, layoutParams)

    }

    private fun dpToPx(dp: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
            .roundToInt()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}