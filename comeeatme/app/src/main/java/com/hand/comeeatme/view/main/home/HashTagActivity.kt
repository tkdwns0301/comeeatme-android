package com.hand.comeeatme.view.main.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
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
import kotlin.math.roundToInt

class HashTagActivity : AppCompatActivity() {
    private var _binding: ActivityHashtagBinding? = null
    private val binding get() = _binding!!

    private lateinit var moodFL: FlexboxLayout
    private lateinit var priceFL: FlexboxLayout
    private lateinit var etcFL: FlexboxLayout

    private val moodList: Array<String> = arrayOf("혼밥", "데이트", "단체모임", "특별한 날", "감성있는")
    private val priceList: Array<String> = arrayOf("가성비", "자극적인", "고급스러운", "신선한 재료", "시그니쳐 메뉴")
    private val etcList: Array<String> = arrayOf("친절", "청결", "반려동물 동반", "아이 동반", "24시간", "주차장")
    private var checkedChipList = ArrayList<Chip>()
    private var checkedChipNames: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHashtagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkedChipNames = intent.getStringArrayListExtra("checkedChip") as ArrayList<String>

        Log.e("checkedChipNames", "$checkedChipNames")

        initView()
        initListener()
    }

    private fun initView() {
        moodList.forEach {
            if (checkedChipNames!!.contains(it)) {
                binding.flMood.addItem(it, true)
            } else {
                binding.flMood.addItem(it, false)
            }

        }

        priceList.forEach {
            if (checkedChipNames!!.contains(it)) {
                binding.flPrice.addItem(it, true)
            } else {
                binding.flPrice.addItem(it, false)
            }
        }

        etcList.forEach {
            if (checkedChipNames!!.contains(it)) {
                binding.flEtc.addItem(it, true)
            } else {
                binding.flEtc.addItem(it, false)
            }
        }


    }


    private fun initListener() {
        binding.toolbarHashTag.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_Finish -> {
                    // TODO 해쉬태그 검색 결과 전송
                    val intent = Intent(this, HomeFragment::class.java)
                    intent.putExtra("checkedChip", checkedChipNames)
                    setResult(RESULT_OK, intent)
                    finish()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)

                }
            }
        }

        binding.toolbarHashTag.setNavigationOnClickListener {
            // TODO 해쉬태그 검색 결과 전송
            val intent = Intent(this, HomeFragment::class.java)
            intent.putExtra("checkedChip", checkedChipNames)
            setResult(RESULT_OK, intent)
            finish()
        }


    }

    private fun FlexboxLayout.addItem(tag: String, isSelected: Boolean) {

        val chip = LayoutInflater.from(context).inflate(R.layout.layout_chip_custom, null) as Chip

        chip.apply {
            text = "$tag"
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

            val nonCLickTextColor = ContextCompat.getColor(context, R.color.gray2)
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

            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // TODO 체크 했을 때 처리
                    if (!checkedChipNames!!.contains(tag)) {
                        checkedChipNames!!.add(tag)
                    }
                } else {
                    // TODO 체크 풀었을 때 처리
                    checkedChipList.remove(chip)
                    checkedChipNames!!.remove(tag)
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