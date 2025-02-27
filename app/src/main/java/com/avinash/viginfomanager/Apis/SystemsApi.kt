package com.avinash.viginfomanager.Apis

import com.avinash.viginfomanager.Apis.Responses.NormResponse
import com.avinash.viginfomanager.Apis.Responses.SystemInfo
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface SystemsApi {
    @GET("systems/{id}")
    fun getSystem(@Path("id") id: Int): Call<SystemInfo>


    @PATCH("systems/{id}")
    fun updateSystem(@Path("id") id: Int, @Body jsonObject: JsonObject): Call<NormResponse>

}