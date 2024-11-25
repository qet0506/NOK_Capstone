package com.example.nok.ui

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("get_data/{username}")
    fun getUserData(@Path("username") username: String): Call<UserDataClass>
}