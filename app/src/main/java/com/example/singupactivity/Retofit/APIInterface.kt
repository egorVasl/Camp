package com.example.singupactivity.Retofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {
    @POST("/api/account/signin")
    fun login(@Body loginRequest: LoginRequest): Call<TokenResponse>

    @POST("/api/account/signup")
    fun singUp(@Body singUpRequest: SingUpRequest): Call<TokenResponse>

    @GET()
    fun getUsers() : Call<MutableList<LoginResponse>>
}