package com.avinash.viginfomanager.Apis

import com.avinash.viginfomanager.Apis.Responses.NormResponse
import com.avinash.viginfomanager.Apis.Responses.Report
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportsApi {
    @POST("reports/default?")
    fun getDefaultReports(@Query("block_id") blockId: Int,@Body jsonObject: JsonObject): Call<Report>

}