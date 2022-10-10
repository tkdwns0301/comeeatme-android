package com.hand.comeeatme.view.main.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.hand.comeeatme.R
import com.hand.comeeatme.adapter.NewPostImagesAdapter
import com.hand.comeeatme.databinding.ActivityNewpostBinding
import java.io.File
import kotlin.math.roundToInt

class NewPostActivity : AppCompatActivity() {
    private var _binding: ActivityNewpostBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewPostImagesAdapter
    private lateinit var cancel: TextView
    private lateinit var locationSearch: LinearLayout
    private lateinit var radioGroup: RadioGroup

    private var images = ArrayList<Uri>()
    private var checkedImageList = ArrayList<String>()
    private var imagePositionList = ArrayList<Int>()
    private val REQ_STORAGE_PERMISSION = 201
    private val checkedChipList = ArrayList<Chip>()
    private val moodList: Array<String> = arrayOf("혼밥", "데이트", "단체모임", "특별한 날", "감성있는")
    private val priceList: Array<String> = arrayOf("가성비", "자극적인", "고급스러운", "신선한 재료", "시그니쳐 메뉴")
    private val etcList: Array<String> = arrayOf("친절", "청결", "반려동물 동반", "아이 동반", "24시간", "주차장")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityNewpostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
        initListener()

    }

    private fun initView() {
        recyclerView = binding.rvSelectedImages

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        cancel = binding.tvCancel

        locationSearch = binding.llLocation

        binding.icHashTag.flMood.addItems(moodList)
        binding.icHashTag.flPrice.addItems(priceList)
        binding.icHashTag.flEtc.addItems(etcList)

    }

    private fun initListener() {
        binding.btnImage.setOnClickListener {
            val readPermission = ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (readPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQ_STORAGE_PERMISSION
                )
            } else {
                val intent = Intent(this, AlbumActivity::class.java)
                intent.putExtra("checkedImages", checkedImageList)
                intent.putExtra("imagePosition", imagePositionList)
                startActivityForResult(intent, 100)
            }
        }

        cancel.setOnClickListener {
            finish()
        }

        locationSearch.setOnClickListener {
            if(binding.icLocation.clLocation.visibility == View.GONE) {
                binding.icLocation.clLocation.visibility = View.VISIBLE
                binding.ibLocation.setImageResource(R.drawable.ic_arrow_up)
            } else if(binding.icLocation.clLocation.visibility == View.VISIBLE) {
                binding.ibLocation.setImageResource(R.drawable.ic_arrow_down)
                binding.icLocation.clLocation.visibility = View.GONE
            }
        }

        binding.llHashTag.setOnClickListener {
            if(binding.icHashTag.clHashTag.visibility == View.GONE) {
                binding.icHashTag.clHashTag.visibility = View.VISIBLE
                binding.ibHashTag.setImageResource(R.drawable.ic_arrow_up)
            } else if(binding.icHashTag.clHashTag.visibility == View.VISIBLE) {
                binding.ibHashTag.setImageResource(R.drawable.ic_arrow_down)
                binding.icHashTag.clHashTag.visibility = View.GONE
            }
        }

        binding.icLocation.etSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.icLocation.etSearch.text.isEmpty()) {
                    binding.icLocation.ibSearchDelete.visibility = View.GONE
                } else {
                    if(binding.icLocation.ibSearchDelete.visibility != View.VISIBLE) {
                        binding.icLocation.ibSearchDelete.visibility = View.VISIBLE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.icLocation.ibSearchDelete.setOnClickListener {
            binding.icLocation.etSearch.text = null
            softKeyboardHide()
        }

        binding.icLocation.ibSearch.setOnClickListener {
            // TODO 검색처리
            val search: String = binding.icLocation.etSearch.text.toString()
            Log.e("Search", "$search")
        }

        binding.icHashTag.clInit.setOnClickListener {
            for (i in 0 until checkedChipList.size) {
                checkedChipList[0].isChecked = false
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        softKeyboardHide()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 100) {
            checkedImageList = data!!.getStringArrayListExtra("checkedImages") as ArrayList<String>
            imagePositionList = data!!.getIntegerArrayListExtra("imagePosition") as ArrayList<Int>

            images = ArrayList()

            checkedImageList!!.forEach {
                images.add(Uri.fromFile(File(it)))
            }

            setAdapter()
        }
    }

    private fun FlexboxLayout.addItems(tags: Array<String>) {
        for(tag in tags) {
            val chip = LayoutInflater.from(context).inflate(R.layout.layout_chip_custom, null) as Chip

            chip.apply {
                text = "$tag"
                textSize = 14f
                textAlignment = View.TEXT_ALIGNMENT_CENTER

                val nonClickBackground = ContextCompat.getColor(context, R.color.gray0)
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
                checkedIcon = null
                isCheckable = true

                setOnCheckedChangeListener { buttonView, isChecked ->
                    if(isChecked) {
                        // TODO 체크 했을 때 처리
                        checkedChipList.add(chip)
                    } else {
                        // TODO 체크 풀었을 때 처리
                        checkedChipList.remove(chip)
                    }
                }
            }

            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )

            layoutParams.rightMargin = dpToPx(10)
            addView(chip, childCount-1, layoutParams)

        }


    }

    private fun softKeyboardHide() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.icLocation.etSearch.windowToken, 0)
    }

    private fun setAdapter() {
        if (images != null) {
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            adapter = NewPostImagesAdapter(images, onClickDeleteIcon = {
                deleteTask(it)
            })
            recyclerView.adapter = adapter
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }
    }

    private fun deleteTask(position: Int) {
        images.removeAt(position)
        checkedImageList.removeAt(position)
        adapter.notifyDataSetChanged()
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