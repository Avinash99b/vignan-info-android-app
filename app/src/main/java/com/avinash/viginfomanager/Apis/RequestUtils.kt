package com.avinash.viginfomanager.Apis

import com.avinash.viginfomanager.Apis.Responses.AuthToken
import com.avinash.viginfomanager.Apis.Responses.Error
import com.google.gson.Gson
import retrofit2.Response

object RequestUtils
{
    private val gson = Gson()
    fun parseError(response:Response<*>): String
    {
        val errorResponse = response.errorBody()?.string()
        return gson.fromJson(errorResponse, Error::class.java).message
    }
}