package com.example.tiktokdownloaderdemo.data.listener

import com.example.tiktokdownloaderdemo.model.VideoModel

interface ApiResponse {

    fun videoResponse(video: VideoModel)
}