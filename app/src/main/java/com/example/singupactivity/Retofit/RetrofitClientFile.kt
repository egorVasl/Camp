package com.example.singupactivity.Retofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientFile {
    private var retrofit: Retrofit? = null
    private const val baseUrl = "http://junior.balinasoft.com"
    private fun getClient(baseUrl: String?) : Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    val api: APIInterface = getClient(baseUrl).create(APIInterface::class.java)



}