package com.avinash.viginfomanager.Apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Apis {
    companion object{
        val BASE_URL = "https://vigilant-robot-7f1b7e.netlify.app/.netlify/functions/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val UsersApi = retrofit.create(UsersApi::class.java)
    }
}