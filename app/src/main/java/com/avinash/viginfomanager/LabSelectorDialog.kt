package com.avinash.viginfomanager

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.avinash.viginfomanager.Adapters.LabsAdapter
import com.avinash.viginfomanager.Apis.Apis
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.Apis.RequestUtils
import com.avinash.viginfomanager.Apis.Responses.Lab
import com.avinash.viginfomanager.databinding.BottomSheetSelectorBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LabSelectorDialog(val activity: Activity): BottomSheetDialog(activity) {
    private val binding: BottomSheetSelectorBinding = BottomSheetSelectorBinding.inflate(layoutInflater)

    val progressDialog = ProgressDialog(activity).apply {
        setMessage("Please wait...")
        setCancelable(false)
    }

    private val labsAdapter = LabsAdapter(activity)
    private val dataManager = DataManager.getInstance(activity)
    init {
        setContentView(binding.root)
        binding.bottomSheetTitle.text = "Select Lab"
        binding.bottomSheetRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.bottomSheetRecyclerView.adapter = labsAdapter
    }

    @Deprecated("Use show(blockId:Int,floorNo:Int) instead",
        ReplaceWith("super.show()", "com.google.android.material.bottomsheet.BottomSheetDialog"))
    override fun show() {
        super.show()
    }

    fun show(blockId: Int){

        val block = dataManager.blocks.value?.find { it.id == blockId }
        if(block == null){
            Toast.makeText(activity, "Failed to load labs", Toast.LENGTH_SHORT).show()
            return
        }
        val noOfFloors = block.floors
        val items = ArrayList<String>()
        for(i in 0 until noOfFloors){
            items.add("Floor ${i+1}")
        }
        items.add("All Floors")

        val builder = AlertDialog.Builder(activity)
            .setTitle("Select Floor")
            .setSingleChoiceItems(items.toTypedArray(), -1){ dialog, which ->
                dialog.dismiss()
                loadLabs(blockId, if(which == noOfFloors) 0 else which+1)

                super.show()
            }

        builder.show()
    }

    private fun loadLabs(blockId:Int, floorNo:Int=0){
        progressDialog.show()

        Apis.LabsApi.getLabs(blockId, ArrayList<Int>().apply { add(floorNo)
            add(floorNo) }).enqueue(object :Callback<ArrayList<Lab>>{
            override fun onResponse(p0: Call<ArrayList<Lab>>, p1: Response<ArrayList<Lab>>) {
                progressDialog.dismiss()
                if(p1.isSuccessful){
                    val labs = p1.body()
                    Log.e("Labs", labs.toString())
                    if(labs!=null){
                        labsAdapter.labs = labs
                    }else{
                        Toast.makeText(activity, "Failed to load labs", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(activity, RequestUtils.parseError(p1), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<ArrayList<Lab>>, p1: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(activity, "Failed to load labs", Toast.LENGTH_SHORT).show()
            }

        })
    }
}