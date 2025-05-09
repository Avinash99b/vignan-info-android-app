package com.avinash.viginfomanager.Apis

import com.avinash.viginfomanager.Apis.Responses.Problem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Apis {
    companion object{
        private const val BASE_URL = "http://3.110.20.195/"

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val UsersApi = retrofit.create(UsersApi::class.java)

        val SystemsApi = retrofit.create(SystemsApi::class.java)

        val BlocksApi = retrofit.create(BlocksApi::class.java)

        val LabsApi = retrofit.create(LabsApi::class.java)

        val ReportsApi = retrofit.create(ReportsApi::class.java)

        val ProblemsApi = retrofit.create(ProblemsApi::class.java)
    }
}