package com.example.tiktokdownloaderdemo.data.repository

import com.example.tiktokdownloaderdemo.model.VideoModel
import com.example.tiktokdownloaderdemo.data.listener.ApiResponse
import com.example.tiktokdownloaderdemo.data.remote.TiktokApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TikTokRepositoryImpl(val api: TiktokApi) : ApiRepository {

    private lateinit var listener: ApiResponse

    //    val res = Gson().fromJson(t.get("data").asJsonObject.get("play"),String::class.java)
    override suspend fun getVideoData(videoUrl: String) {
            val response = api.getDownloadUrls(videoUrl)

            if (response.isSuccessful) {
                val body = response.body()!!
               val video = if (body.get("code").asInt == 0){
                  val url =  body.get("data").asJsonObject.get("play").asString
                   val id = body.get("data").asJsonObject.get("id").asString
                   val title = body.get("data").asJsonObject.get("title").asString
                   VideoModel(title,url,id)
                }else{
                    VideoModel("","","")
                }
                withContext(Dispatchers.Main) {
                    listener.videoResponse(video)
                }

            }



    }

    override fun setApiListener(apiResponse: ApiResponse) {
        listener = apiResponse
    }
}