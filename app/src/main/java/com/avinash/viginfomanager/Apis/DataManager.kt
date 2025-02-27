package com.avinash.viginfomanager.Apis

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.avinash.viginfomanager.Apis.Responses.Block
import com.avinash.viginfomanager.Apis.Responses.SystemInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataManager(val sharedPreferences: SharedPreferences) {

    val blocks = MutableLiveData<ArrayList<Block>?>(ArrayList())

    val prevVisitedSystems = MutableLiveData<ArrayList<SystemInfo>>(ArrayList())

    private val gson = Gson()

    init {
        Apis.BlocksApi.getBlocks().enqueue(object : retrofit2.Callback<ArrayList<Block>> {
            override fun onResponse(call: retrofit2.Call<ArrayList<Block>>, response: retrofit2.Response<ArrayList<Block>>) {
                blocks.value = response.body()
            }

            override fun onFailure(call: retrofit2.Call<ArrayList<Block>>, t: Throwable) {
                blocks.value = null
            }
        })

        val prevSysData = sharedPreferences.getString("prevVisitedSystems","")
        prevVisitedSystems.postValue(gson.fromJson(prevSysData,object :TypeToken<ArrayList<SystemInfo>>(){}.type))

    }

    fun addPrevVisitedSystem(systemInfo: SystemInfo){
        var prevSys = prevVisitedSystems.value ?: ArrayList()
        prevSys = prevSys.filter {
            it.id != systemInfo.id
        } as ArrayList<SystemInfo>
        prevSys.add(systemInfo)
        prevVisitedSystems.postValue(prevSys)
        sharedPreferences.edit().putString("prevVisitedSystems",gson.toJson(prevSys)).apply()
    }

    companion object{
        @JvmStatic
        lateinit var instance:DataManager

        @JvmStatic
        fun getInstance(activity: Activity):DataManager{
            if(!::instance.isInitialized){
                instance = DataManager(activity.getSharedPreferences("vig",Context.MODE_PRIVATE))
            }
            return instance
        }
    }
}