package com.example.tiktokdownloaderdemo.application

import com.example.tiktokdownloaderdemo.data.remote.TiktokApi
import com.example.tiktokdownloaderdemo.data.repository.ApiRepository
import com.example.tiktokdownloaderdemo.data.repository.TikTokRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): TiktokApi {
        val inspector = HttpLoggingInterceptor()
        inspector.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
        client.addInterceptor(inspector)
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .baseUrl("https://www.tikwm.com/")
            .build()
            .create(TiktokApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: TiktokApi): ApiRepository {
        return TikTokRepositoryImpl(api)
    }



}