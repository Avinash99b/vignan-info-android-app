package com.avinash.viginfomanager.Apis

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.avinash.viginfomanager.Apis.Responses.Block
import com.avinash.viginfomanager.Apis.Responses.SystemInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataManager(private val sharedPreferences: SharedPreferences) {

    enum class DashboardSystemShowMode {
        PREV_VISITED, BOOKMARKED
    }

    val blocks = MutableLiveData<ArrayList<Block>?>(ArrayList())

    val dashboardShowSystems = MutableLiveData<ArrayList<SystemInfo>>(ArrayList())

    private val gson = Gson()

    var dashboardSystemsMode = MutableLiveData(DashboardSystemShowMode.PREV_VISITED)

    init {
        Apis.BlocksApi.getBlocks().enqueue(object : retrofit2.Callback<ArrayList<Block>> {
            override fun onResponse(
                call: retrofit2.Call<ArrayList<Block>>,
                response: retrofit2.Response<ArrayList<Block>>
            ) {
                blocks.value = response.body()
            }

            override fun onFailure(call: retrofit2.Call<ArrayList<Block>>, t: Throwable) {
                t.printStackTrace()
                blocks.value = null
            }
        })
        refreshSystems()
    }

    fun getBookmarkedSystems(): ArrayList<SystemInfo> {
        var systemsString = sharedPreferences.getString("bookmarkSystems", "")
        if (systemsString.isNullOrEmpty()) {
            systemsString = "[]"
        }
        return gson.fromJson(
            systemsString, object : TypeToken<ArrayList<SystemInfo>>() {}.type
        )
    }

    fun refreshSystems() {
        var prevSysData = when (dashboardSystemsMode.value) {
            DashboardSystemShowMode.PREV_VISITED -> sharedPreferences.getString(
                "prevVisitedSystems",
                ""
            )

            DashboardSystemShowMode.BOOKMARKED -> sharedPreferences.getString("bookmarkSystems", "")
            null -> sharedPreferences.getString("prevVisitedSystems", "")
        }

        if (prevSysData.isNullOrEmpty()) {
            prevSysData = "[]"
        }
        dashboardShowSystems.postValue(
            gson.fromJson(
                prevSysData, object : TypeToken<ArrayList<SystemInfo>>() {}.type
            )
        )
    }

    fun addPrevVisitedSystem(systemInfo: SystemInfo) {
        val prevVisitedSystemsString = sharedPreferences.getString("prevVisitedSystems", "")
        var prevSys: ArrayList<SystemInfo> = gson.fromJson(
            prevVisitedSystemsString, object : TypeToken<ArrayList<SystemInfo>>() {}.type
        ) ?: ArrayList();
        prevSys = prevSys.filter {
            it.id != systemInfo.id
        } as ArrayList<SystemInfo>
        prevSys.add(systemInfo)
        sharedPreferences.edit().putString("prevVisitedSystems", gson.toJson(prevSys)).apply()
        if (dashboardSystemsMode.value == DashboardSystemShowMode.PREV_VISITED) {
            dashboardShowSystems.postValue(prevSys)
        }
    }

    fun addBookmarkSystem(systemInfo: SystemInfo) {
        val prevVisitedSystemsString = sharedPreferences.getString("bookmarkSystems", "")
        var prevSys: ArrayList<SystemInfo> = gson.fromJson(
            prevVisitedSystemsString, object : TypeToken<ArrayList<SystemInfo>>() {}.type
        ) ?: ArrayList();
        prevSys = prevSys.filter {
            it.id != systemInfo.id
        } as ArrayList<SystemInfo>
        prevSys.add(systemInfo)
        sharedPreferences.edit().putString("bookmarkSystems", gson.toJson(prevSys)).apply()
        if (dashboardSystemsMode.value == DashboardSystemShowMode.BOOKMARKED) {
            dashboardShowSystems.postValue(prevSys)
        }
    }

    fun removeBookmarkSystem(systemInfo: SystemInfo) {
        val prevVisitedSystemsString = sharedPreferences.getString("bookmarkSystems", "")
        var prevSys: ArrayList<SystemInfo> = gson.fromJson(
            prevVisitedSystemsString, object : TypeToken<ArrayList<SystemInfo>>() {}.type
        ) ?: ArrayList();
        prevSys = prevSys.filter {
            it.id != systemInfo.id
        } as ArrayList<SystemInfo>
        sharedPreferences.edit().putString("bookmarkSystems", gson.toJson(prevSys)).apply()
        if (dashboardSystemsMode.value == DashboardSystemShowMode.BOOKMARKED) {
            dashboardShowSystems.postValue(prevSys)
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: DataManager

        @JvmStatic
        fun getInstance(activity: Activity): DataManager {
            if (!::instance.isInitialized) {
                instance = DataManager(activity.getSharedPreferences("vig", Context.MODE_PRIVATE))
            }
            return instance
        }
    }
}