package com.example.tiktokdownloaderdemo.ui

import android.Manifest
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.tiktokdownloaderdemo.model.VideoModel
import com.example.tiktokdownloaderdemo.adapter.MyAdapter
import com.example.tiktokdownloaderdemo.databinding.ActivityMainBinding
import com.example.tiktokdownloaderdemo.util.State
import com.example.tiktokdownloaderdemo.util.rootFile
import com.example.tiktokdownloaderdemo.util.saveVideo
import com.example.tiktokdownloaderdemo.viewmodels.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myViewModel: MyViewModel


    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }
    private val TAG = "---MainActivity"

    private var fileList: ArrayList<File> = ArrayList()
    private lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]


        binding.downloadBtn.setOnClickListener {
            val url = binding.videoUrl.editText!!.text.toString()
            if (url.isNotEmpty() && url.contains("tiktok")) {
                myViewModel.getVideoData(url)
            } else Toast.makeText(this, "Enter Valid Url!!", Toast.LENGTH_SHORT).show()

        }

        getReceivedData()

        initRecyclerView()


        myViewModel.responseLiveData.observe(this) {
            if (checkIfFileExist(it)) {
                Toast.makeText(this@MainActivity, "File Already Exist", Toast.LENGTH_SHORT).show()
                return@observe
            }
            Toast.makeText(this@MainActivity, "Downloading..", Toast.LENGTH_SHORT).show()

            Glide.with(this).asFile().load(it.videoUrl).into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {

                    GlobalScope.launch(Dispatchers.IO) {
                        val finished = async { saveVideo(resource, it, this@MainActivity) }
                        val state = finished.await()
                        if ( state is State.COMPLETE) {

                            refreshFiles()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Download Complete\n${state.path}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {Toast.makeText(
                            this@MainActivity,
                            "Download Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                        }

                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
        }


    }

    private fun getReceivedData() {
        when (intent?.action) {
            Intent.ACTION_SEND -> {

                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    binding.videoUrl.editText!!.setText(it)
                    binding.downloadBtn.performClick()
                }

            }
        }
    }

    private fun initRecyclerView() {

        myAdapter = MyAdapter(fileList)

        binding.downloaded.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = myAdapter
        }

    }

   suspend fun refreshFiles() {
        fileList.clear()
        rootFile.listFiles()?.let { f ->

            f.forEach {
                fileList.add(it)
            }
        }
        withContext(Dispatchers.Main){
            myAdapter.notifyDataSetChanged()
        }

    }


    private fun checkIfFileExist(it: VideoModel): Boolean {
        val saveFile = File(rootFile, it.id + ".mp4")
        return saveFile.exists()
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            refreshFiles()
        }
    }


}