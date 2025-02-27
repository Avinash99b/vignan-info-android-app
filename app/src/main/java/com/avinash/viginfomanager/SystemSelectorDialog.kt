package com.avinash.viginfomanager

import android.app.Activity
import android.app.ProgressDialog
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.avinash.viginfomanager.Adapters.SystemsAdapter
import com.avinash.viginfomanager.Apis.Apis
import com.avinash.viginfomanager.databinding.RecyclerSystemLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.avinash.viginfomanager.Apis.Responses.System
import com.avinash.viginfomanager.databinding.BottomSheetSelectorBinding

class SystemSelectorDialog(val activity: Activity):BottomSheetDialog(activity) {
    private val binding: BottomSheetSelectorBinding = BottomSheetSelectorBinding.inflate(layoutInflater)
    private val progressDialog = ProgressDialog(activity).apply {
        setMessage("Please wait...")
        setCancelable(false)
    }
    private val systemsAdapter = SystemsAdapter(activity)

    init {
        setContentView(binding.root)
        binding.bottomSheetTitle.text = "Select System"
        binding.bottomSheetRecyclerView.layoutManager = GridLayoutManager(activity,4)
        binding.bottomSheetRecyclerView.adapter = systemsAdapter
    }
    @Deprecated("Use show(blockId:Int,floorNo:Int) instead",
        ReplaceWith("super.show()", "com.google.android.material.bottomsheet.BottomSheetDialog"))
    override fun show() {
        super.show()
    }

    fun show(labId:Int){
        super.show()
        loadSystems(labId)
    }

    private fun loadSystems(labId:Int){
        progressDialog.show()
        Apis.LabsApi.getSystems(labId).enqueue(object : Callback<ArrayList<System>> {
            override fun onResponse(call: Call<ArrayList<System>>, response: Response<ArrayList<System>>) {
                if(response.isSuccessful){
                    val systems = response.body()
                    if(systems != null){
                        systemsAdapter.systems = systems
                    }
                }
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<ArrayList<System>>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(activity, "Failed to load systems", Toast.LENGTH_SHORT).show()
            }
        })
    }
}