package com.avinash.viginfomanager.Apis

import com.avinash.viginfomanager.Apis.Responses.AuthToken
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersApi {
    @POST("users/login")
    fun login(@Body jsonObject: JsonObject): Call<AuthToken>
}