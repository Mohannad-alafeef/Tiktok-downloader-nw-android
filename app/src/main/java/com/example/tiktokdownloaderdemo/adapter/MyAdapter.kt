package com.example.tiktokdownloaderdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiktokdownloaderdemo.databinding.VideoItemBinding

import java.io.File

class MyAdapter(private val fileList: ArrayList<File>):RecyclerView.Adapter<MyAdapter.ItemHolder>() {

   inner class ItemHolder(private val v: VideoItemBinding):RecyclerView.ViewHolder(v.root) {
        fun  bind(p:Int){
            Glide.with(v.videoImage.context).load(fileList[p]).into(v.videoImage)
            v.title.text = fileList[p].name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder = ItemHolder(
        VideoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = fileList.size
}