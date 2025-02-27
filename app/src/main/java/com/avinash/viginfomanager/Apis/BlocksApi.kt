package com.avinash.viginfomanager.Apis

import com.avinash.viginfomanager.Apis.Responses.AuthToken
import com.avinash.viginfomanager.Apis.Responses.Block
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BlocksApi {
    @GET("blocks")
    fun getBlocks(): Call<ArrayList<Block>>
}