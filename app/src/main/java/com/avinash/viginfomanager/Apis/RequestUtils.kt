package com.avinash.viginfomanager.Apis

import android.util.Log
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
        return try{
            gson.fromJson(errorResponse, Error::class.java).message
        }catch (e:Exception){
            response.errorBody()?.string()?.let { Log.e("RequestUtils", it) }
            errorResponse.toString()
        }
    }
}