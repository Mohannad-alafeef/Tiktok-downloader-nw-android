package com.example.tiktokdownloaderdemo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktokdownloaderdemo.model.VideoModel
import com.example.tiktokdownloaderdemo.data.listener.ApiResponse
import com.example.tiktokdownloaderdemo.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel(), ApiResponse {

    private val _responseLiveData = MutableLiveData<VideoModel>()
    val responseLiveData: LiveData<VideoModel> = _responseLiveData


    init {
        apiRepository.setApiListener(this)
    }

    fun getVideoData(url: String) {
        viewModelScope.launch{
            apiRepository.getVideoData(url)
        }
    }

    override fun videoResponse(t: VideoModel) {
        _responseLiveData.value = t
    }

}