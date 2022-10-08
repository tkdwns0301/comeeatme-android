package com.hand.comeeatme.view.main.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.adapter.NewPostImagesAdapter
import com.hand.comeeatme.databinding.ActivityNewpostBinding
import java.io.File

class NewPostActivity : AppCompatActivity() {
    private var _binding: ActivityNewpostBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewPostImagesAdapter
    private lateinit var cancel: TextView

    private var images = ArrayList<Uri>()
    private var checkedImageList = ArrayList<String>()
    private var imagePositionList = ArrayList<Int>()

    private val REQ_STORAGE_PERMISSION = 201


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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}