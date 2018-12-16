package com.hamletleon.randomusers.repositories.remote.core

import com.hamletleon.randomusers.dtos.UserDto
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private lateinit var mHttpClient: OkHttpClient
    private var mRetrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit? {
        if (mRetrofit == null){
            mHttpClient = getHttpClient()
            mRetrofit = getRetrofit(baseUrl)
        }
        return mRetrofit
    }

    private fun getHttpClient(): OkHttpClient{
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        // Adding include parameter to all requests
        httpClient.addInterceptor { it ->
            val original = it.request()
            val originalUrl = original.url()
            val url = originalUrl.newBuilder()
                .addQueryParameter("inc", UserDto::class.java.declaredFields.filter { !it.isSynthetic && it.name != "serialVersionUID" }.map { it.name }.toString())
                .build()
            val request = original.newBuilder()
                .url(url).build()
            it.proceed(request)
        }

        // Debug Interceptor
//        if (BuildConfig.DEBUG) {
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//            httpClient.addInterceptor(interceptor)
//        }

        return httpClient.build()
    }

    private fun getRetrofit(baseUrl: String): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mHttpClient)
            .build()
    }
}