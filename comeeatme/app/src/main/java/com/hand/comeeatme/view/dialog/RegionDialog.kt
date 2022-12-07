package com.hand.comeeatme.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.DialogRegionBinding

class RegionDialog(
    context: Context,
    s: String,
    g: String,
) : Dialog(context) {
    private lateinit var binding: DialogRegionBinding

    private var s = s
    private var g = g

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private val sido = arrayListOf(
        "서울",
        "경기",
        "인천",
        "강원",
        "대전",
        "세종",
        "충남",
        "충북",
        "부산",
        "울산",
        "경남",
        "경북",
        "대구",
        "광주",
        "전남",
        "전북",
        "제주"
    )

    private val seoul = arrayListOf(
        "전체",
        "강남구",
        "강동구",
        "강북구",
        "강서구",
        "관악구",
        "광진구",
        "구로구",
        "금천구",
        "노원구",
        "도봉구",
        "동대문구",
        "동작구",
        "마포구",
        "서대문구",
        "서초구",
        "성동구",
        "성북구",
        "송파구",
        "양천구",
        "영등포구",
        "용산구",
        "은평구",
        "종로구",
        "중구",
        "중랑구"
    )

    private val gyeonggi = arrayListOf(
        "전체",
        "가평군",
        "고양시 덕양구",
        "고양시 일산동구",
        "고양시 일산서구",
        "과천시",
        "광명시",
        "광주시",
        "구리시",
        "군포시",
        "김포시",
        "남양주시",
        "동두천시",
        "부천시",
        "성남시 분당구",
        "성남시 수정구",
        "성남시 중원구",
        "수원시 권선구",
        "수원시 영통구",
        "수원시 장안구",
        "수원시 팔달구",
        "시흥시",
        "안산시 단원구",
        "안산시 상록구",
        "안성시",
        "안양시 동안구",
        "안양시 만안구",
        "양주시",
        "양평군",
        "여주시",
        "연천군",
        "오산시",
        "용인시 기흥구",
        "용인시 수지구",
        "용인시 처인구",
        "의왕시",
        "의정부시",
        "이천시",
        "파주시",
        "평택시",
        "포천시",
        "하남시",
        "화성시"
    )

    private val incheon = arrayListOf(
        "전체", "강화군", "계양구", "남동구", "동구", "미추홀구", "부평구", "서구", "연수구", "옹진군", "중구"
    )

    private val gangwon = arrayListOf(
        "전체",
        "강릉시",
        "고성군",
        "동해시",
        "삼척시",
        "속초시",
        "양구군",
        "양양군",
        "영월군",
        "원주시",
        "인제군",
        "정선군",
        "철원군",
        "춘천시",
        "태백시",
        "평창군",
        "홍천군",
        "화천군",
        "횡성군"
    )

    private val daejeon = arrayListOf(
        "전체", "대덕구", "동구", "서구", "유성구", "중구"
    )

    private val sejong = arrayListOf(
        "전체", "세종시"
    )

    private val chungnam = arrayListOf(
        "전체",
        "계룡시",
        "공주시",
        "금산군",
        "논산시",
        "당진시",
        "보령시",
        "부여군",
        "서산시",
        "서천군",
        "아산시",
        "예산군",
        "천안시 동남구",
        "천안시 서북구",
        "청양군",
        "태안군",
        "홍성군"
    )

    private val chungbuk = arrayListOf(
        "전체",
        "괴산군",
        "단양군",
        "보은군",
        "영동군",
        "옥천군",
        "음성군",
        "제천시",
        "증평군",
        "진천군",
        "청주시 상당구",
        "청주시 서원구",
        "청주시 청원구",
        "청주시 흥덕구",
        "충주시"
    )

    private val busan = arrayListOf(
        "전체",
        "강서구",
        "금정구",
        "기장군",
        "남구",
        "동구",
        "동래구",
        "부산진구",
        "북구",
        "사상구",
        "사하구",
        "서구",
        "수영구",
        "연제구",
        "영도구",
        "중구",
        "해운대구"
    )

    private val ulsan = arrayListOf(
        "전체", "남구", "동구", "북구", "울주군", "중구"
    )

    private val gyeongnam = arrayListOf(
        "전체",
        "거제시",
        "거창군",
        "고성군",
        "김해시",
        "남해군",
        "밀양시",
        "사천시",
        "산청군",
        "양산시",
        "의령군",
        "진주시",
        "창년군",
        "창원시 마산합포구",
        "창원시 마산회원구",
        "창원시 성산구",
        "창원시 의창구",
        "창원시 진해구",
        "통영시",
        "하동군",
        "함안군",
        "함양군",
        "합천군"
    )

    private val gyeongbuk = arrayListOf(
        "전체",
        "경산시",
        "경주시",
        "고령군",
        "구미시",
        "군위군",
        "김천시",
        "문경시",
        "봉화군",
        "상주시",
        "성주군",
        "안동시",
        "영덕군",
        "영양군",
        "영주시",
        "영천시",
        "예천군",
        "울릉군",
        "울진군",
        "의성군",
        "청도군",
        "청송군",
        "칠곡군",
        "포항시 남구",
        "포항시 북구"
    )

    private val daegu = arrayListOf(
        "전체", "남구", "달서구", "달성군", "동구", "북구", "서구", "수성구", "중구"
    )

    private val gwangju = arrayListOf(
        "전체", "광산구", "남구", "동구", "북구", "서구"
    )

    private val jeonnam = arrayListOf(
        "전체",
        "강진군",
        "고흥군",
        "곡성군",
        "광양시",
        "구례군",
        "나주시",
        "담양군",
        "목포시",
        "무안군",
        "보성군",
        "순천시",
        "신안군",
        "여수시",
        "영광군",
        "영암군",
        "완도군",
        "장성군",
        "장흥군",
        "진도군",
        "함평군",
        "해남군",
        "화순군"
    )

    private val jeonbuk = arrayListOf(
        "전체",
        "고창군",
        "군산시",
        "김제시",
        "남원시",
        "무주군",
        "부안군",
        "순창군",
        "완주군",
        "익산시",
        "임실군",
        "장수군",
        "전주시 덕진구",
        "전주시 완산구",
        "정읍시",
        "진안군"
    )

    private val jeju = arrayListOf(
        "전체", "서귀포시", "제주시"
    )

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = context.resources.displayMetrics.widthPixels
        val height = context.resources.displayMetrics.heightPixels

        window!!.setLayout(
            (width - 200),
            (height - 400)
        )

        setCanceledOnTouchOutside(true)
        setCancelable(true)

        sido.forEach { region ->
            rgSido.addItem(region, true)
        }
    }

    private fun RadioGroup.addItem(region: String, isSido: Boolean) {
        val radioButton: RadioButton = LayoutInflater.from(context)
            .inflate(R.layout.layout_radio_button_custom, null) as RadioButton

        radioButton.apply {
            text = region
            if(region == s) {
                isChecked = true
            }

            if (isSido) {
                setOnCheckedChangeListener {_, isChecked ->
                    if (isChecked) {
                        g = "전체"
                        when (text) {
                            "서울" -> {
                                binding.rgGoo.removeAllViews()
                                seoul.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "경기" -> {
                                binding.rgGoo.removeAllViews()
                                gyeonggi.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "인천" -> {
                                binding.rgGoo.removeAllViews()
                                incheon.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "강원" -> {
                                binding.rgGoo.removeAllViews()
                                gangwon.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "대전" -> {
                                binding.rgGoo.removeAllViews()
                                daejeon.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "세종" -> {
                                binding.rgGoo.removeAllViews()
                                sejong.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "충남" -> {
                                binding.rgGoo.removeAllViews()
                                chungnam.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "충북" -> {
                                binding.rgGoo.removeAllViews()
                                chungbuk.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "부산" -> {
                                binding.rgGoo.removeAllViews()
                                busan.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "울산" -> {
                                binding.rgGoo.removeAllViews()
                                ulsan.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "경남" -> {
                                binding.rgGoo.removeAllViews()
                                gyeongnam.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "경북" -> {
                                binding.rgGoo.removeAllViews()
                                gyeongbuk.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "대구" -> {
                                binding.rgGoo.removeAllViews()
                                daegu.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "광주" -> {
                                binding.rgGoo.removeAllViews()
                                gwangju.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "전남" -> {
                                binding.rgGoo.removeAllViews()
                                jeonnam.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "전북" -> {
                                binding.rgGoo.removeAllViews()
                                jeonbuk.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                            "제주" -> {
                                binding.rgGoo.removeAllViews()
                                jeju.forEach { region ->
                                    binding.rgGoo.addItem(region, false)
                                }
                            }
                        }
                    }
                }
            } else {

            }
        }

        addView(radioButton, childCount, layoutParams)
    }
}