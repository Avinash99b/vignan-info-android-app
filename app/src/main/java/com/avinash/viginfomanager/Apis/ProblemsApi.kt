package com.avinash.viginfomanager.Apis

import com.avinash.viginfomanager.Apis.Responses.AuthToken
import com.avinash.viginfomanager.Apis.Responses.Block
import com.avinash.viginfomanager.Apis.Responses.Lab
import com.avinash.viginfomanager.Apis.Responses.NormResponse
import com.avinash.viginfomanager.Apis.Responses.Problem
import com.avinash.viginfomanager.Apis.Responses.System
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProblemsApi {
    @GET("problems")
    fun getProblems(): Call<ArrayList<Problem>>
    @GET("problems/reports")
    fun getReports(): Call<ArrayList<Problem>>
    @POST("problems/report")
    fun reportProblem(@Body jsonObject: JsonObject,@Query("token") authToken: String): Call<NormResponse>
}