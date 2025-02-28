package com.avinash.viginfomanager.Apis

import com.avinash.viginfomanager.Apis.Responses.AuthToken
import com.avinash.viginfomanager.Apis.Responses.Block
import com.avinash.viginfomanager.Apis.Responses.Lab
import com.avinash.viginfomanager.Apis.Responses.NormResponse
import com.avinash.viginfomanager.Apis.Responses.System
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LabsApi {
    @POST("labs?")
    fun getLabs(@Query("blockId") blockId: Int,
                @Body jsonObject: JsonObject): Call<ArrayList<Lab>>

    @GET("labs/{id}/systems")
    fun getSystems(@Path("id") id: Int): Call<ArrayList<System>>

    @POST("labs/")
    fun addLab(@Body jsonObject: JsonObject): Call<NormResponse>
}