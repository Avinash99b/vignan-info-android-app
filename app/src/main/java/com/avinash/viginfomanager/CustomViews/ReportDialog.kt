package com.avinash.viginfomanager.CustomViews

import android.app.Activity
import android.app.Dialog
import android.view.ViewGroup
import android.widget.Toast
import com.avinash.viginfomanager.Apis.Apis
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.Apis.RequestUtils
import com.avinash.viginfomanager.Apis.Responses.NormResponse
import com.avinash.viginfomanager.databinding.DialogReportSysProblemBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportDialog(activity: Activity) : Dialog(activity) {

    val binding by lazy {
        DialogReportSysProblemBinding.inflate(activity.layoutInflater)
    }

    val dataManager by lazy {
        DataManager.getInstance(activity)
    }

    init {
        setContentView(binding.root)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dataManager.problems.observeForever { it ->
            val problems = it ?: ArrayList()
            binding.edtSysProblem.setAutofillHints(*problems.map { it.name }.toTypedArray())
        }
    }

    fun show(systemId:Int) {

        binding.submitProblemBtn.setOnClickListener{
            if(binding.edtSysProblem.text.isNullOrEmpty()){
                Toast.makeText(context,"Please enter all data",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val jsonObject = JsonObject().apply {
                addProperty("system_id",systemId)
                addProperty("problem",binding.edtSysProblem.text.toString())
            }

            Apis.ProblemsApi.reportProblem(jsonObject,dataManager.authToken).enqueue(object :Callback<NormResponse>{
                override fun onResponse(p0: Call<NormResponse>, p1: Response<NormResponse>) {
                    if(p1.isSuccessful){
                        Toast.makeText(context,"OK",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,RequestUtils.parseError(p1),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(p0: Call<NormResponse>, p1: Throwable) {
                    p1.printStackTrace()
                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                }

            })

        }

        super.show()
    }
}