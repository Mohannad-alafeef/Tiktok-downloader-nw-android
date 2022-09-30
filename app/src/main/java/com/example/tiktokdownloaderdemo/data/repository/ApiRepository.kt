package com.example.tiktokdownloaderdemo.data.repository

import com.example.tiktokdownloaderdemo.data.listener.ApiResponse

interface ApiRepository {

    //Call
    suspend fun getVideoData(videoUrl: String)

    //Response
     fun setApiListener(apiResponse: ApiResponse)

}