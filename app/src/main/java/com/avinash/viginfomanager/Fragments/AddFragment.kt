package com.avinash.viginfomanager.Fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.avinash.viginfomanager.Apis.Apis
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.Apis.RequestUtils
import com.avinash.viginfomanager.Apis.Responses.NormResponse
import com.avinash.viginfomanager.DashboardActivity
import com.avinash.viginfomanager.R
import com.avinash.viginfomanager.databinding.FragmentAddBinding
import com.google.android.material.chip.ChipGroup
import com.google.gson.JsonObject
import retrofit2.Call

class AddFragment : Fragment() {

    val binding by lazy {
        FragmentAddBinding.inflate(layoutInflater)
    }

    private val dataManager by lazy {
        DataManager.getInstance(activity as DashboardActivity)
    }

    private val progressDialog by lazy {
        ProgressDialog(activity as DashboardActivity).apply {
            setCancelable(false)
            setMessage("Please Wait...")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.addModeSelector.isSingleSelection = true
        binding.addModeSelector.isSelectionRequired = true

        binding.addModeSelector.check(R.id.system_chip)
        binding.addModeSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.system_chip -> {
                    binding.addLabContainer.visibility = View.GONE
                    binding.addSystemContainer.visibility = View.VISIBLE
                }

                R.id.lab_chip -> {
                    binding.addSystemContainer.visibility = View.GONE
                    binding.addLabContainer.visibility = View.VISIBLE
                }
            }
        }

        binding.findLabIdBtn.setOnClickListener{
            (activity as DashboardActivity).switchToHome()
        }
        binding.findBlockIdBtn.setOnClickListener{
            (activity as DashboardActivity).switchToHome()
        }

        binding.addLabBtn.setOnClickListener{
            addLab()
        }

        binding.addSystemBtn.setOnClickListener{
            addSystem()
        }

        binding.floorEdt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if(binding.blockIdEdt.text.toString().isEmpty()){
                    binding.blockIdEdt.requestFocus()
                    Toast.makeText(activity, "Please select block first", Toast.LENGTH_SHORT).show()
                    return@OnFocusChangeListener
                }else{
                    val blockId = binding.blockIdEdt.text.toString().toInt()
                    val block = dataManager.blocks.value?.find { it.id == blockId } ?: return@OnFocusChangeListener

                    val noOfFloors = block.floors
                    val items = ArrayList<String>()
                    for(i in 0 until noOfFloors){
                        items.add("Floor ${i+1}")
                    }

                    val builder = AlertDialog.Builder(activity)
                        .setTitle("Select Floor")
                        .setSingleChoiceItems(items.toTypedArray(), -1){ dialog, which ->
                            dialog.dismiss()
                            binding.floorEdt.setText("${which+1}")
                        }

                    builder.show()
                }
            }
        }
        return binding.root
    }

    private fun addLab(){
        val name = binding.labNameEdt.text.toString()
        val floor = binding.floorEdt.text.toString()
        val incharge = binding.inchargeIdEdt.text.toString()
        val description = binding.labDesEdt.text.toString()
        val blockId = binding.blockIdEdt.text.toString().toInt()

        if(name.isEmpty() || floor.isEmpty() || incharge.isEmpty() || description.isEmpty()|| blockId == 0){
            return
        }

        val jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("floor", floor)
            addProperty("incharge_id", incharge)
            addProperty("des", description)
            addProperty("block_id", blockId)
        }

        progressDialog.show()
        Apis.LabsApi.addLab(jsonObject).enqueue(object : retrofit2.Callback<NormResponse> {
            override fun onResponse(p0: Call<NormResponse>, p1: retrofit2.Response<NormResponse>) {
                progressDialog.dismiss()
                if(p1.isSuccessful){
                    val response = p1.body()
                    if(response!=null){
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(activity, RequestUtils.parseError(p1), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<NormResponse>, p1: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun addSystem(){
        val labId = binding.labIdEdt.text.toString().toInt()
        val working = binding.workingSwitch.isChecked

        if(labId == 0){
            return
        }
        val jsonObject = JsonObject().apply {
            addProperty("lab_id", labId)
            addProperty("working", working)
        }

        progressDialog.show()
        Apis.SystemsApi.addSystem(jsonObject).enqueue(object : retrofit2.Callback<NormResponse> {
            override fun onResponse(p0: Call<NormResponse>, p1: retrofit2.Response<NormResponse>) {
                progressDialog.dismiss()
                if(p1.isSuccessful){
                    val response = p1.body()
                    if(response!=null){
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(activity, RequestUtils.parseError(p1), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<NormResponse>, p1: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

