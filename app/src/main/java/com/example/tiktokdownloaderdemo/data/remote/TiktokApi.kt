package com.example.tiktokdownloaderdemo.data.remote

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface TiktokApi {

    @GET("api/")
     suspend fun getDownloadUrls(@Query("url") videoUrl: String): Response<JsonObject>
}