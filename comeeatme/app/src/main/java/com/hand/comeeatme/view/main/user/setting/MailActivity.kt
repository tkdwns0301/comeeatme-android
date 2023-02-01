package com.hand.comeeatme.view.main.user.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hand.comeeatme.databinding.ActivityMailBinding

class MailActivity : AppCompatActivity() {
    companion object {
        const val SEND_EMAIL = "sendEmail"
        fun newIntent(context: Context) = Intent(context, MailActivity::class.java)
    }

    private var _binding: ActivityMailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        toolbarMail.setNavigationOnClickListener {
            finish()
        }

        clSendMail.setOnClickListener {
            sendEmail()
        }
    }

    @SuppressLint("IntentReset")
    private fun sendEmail() {
        val emailAddress = "tkdwns970301@naver.com"
        val title = "[ComeEatMe] 문의사항입니다."

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            type = "text/plain"
            data = Uri.parse("mailto:")

            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        if(intent.resolveActivity(packageManager) != null) {
            startActivityForResult(Intent.createChooser(intent, SEND_EMAIL), 100)
        } else {
            Toast.makeText(this, "메일을 전송할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}