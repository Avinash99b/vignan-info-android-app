package com.avinash.viginfomanager

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.avinash.viginfomanager.Apis.Apis
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.Apis.RequestUtils
import com.avinash.viginfomanager.Apis.Responses.NormResponse
import com.avinash.viginfomanager.Apis.Responses.SystemInfo
import com.avinash.viginfomanager.CustomViews.ReportDialog
import com.avinash.viginfomanager.databinding.ActivitySystemDetailsBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import kotlin.properties.Delegates

class SystemDetailsActivity : AppCompatActivity() {
    private val binding: ActivitySystemDetailsBinding by lazy {
        ActivitySystemDetailsBinding.inflate(layoutInflater)
    }

    val progressDialog by lazy {
        ProgressDialog(this).apply {
            setMessage("Please wait...")
            setCancelable(false)
        }
    }

    val dataManager by lazy {
        DataManager.getInstance(this)
    }

    val reportDialog by lazy {
        ReportDialog(this)
    }
    private var systemId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        systemId = intent.getIntExtra("systemId", 0)

        loadSystemDetails()

        binding.nextSystemTv.setOnClickListener {
            systemId += 1
            loadSystemDetails()
        }
        binding.previousSystemTv.setOnClickListener {
            systemId -= 1
            loadSystemDetails()
        }

        binding.reportSystemBtn.setOnClickListener{
            reportDialog.show(systemId)
        }
        binding.submitBtn.setOnClickListener {

            try {
                val jsonObject = JsonObject().apply {
                    addProperty("working", binding.switchWorking.isChecked)
                    addProperty("download_speed", binding.edtDownloadSpeed.text.toString())
                    addProperty("upload_speed", binding.edtUploadSpeed.text.toString())
                    addProperty("ping", binding.edtPingSpeed.text.toString())
                    addProperty("lab_id", binding.labIdTv.text.toString().toInt())
                    addProperty("keyboard_working", binding.keyboardWorkingSwitch.isChecked)
                    addProperty("mouse_working", binding.mouseWorkingSwitch.isChecked)
                    addProperty("serial_no", binding.edtSerialNo.text.toString())
                    addProperty("processor", binding.edtProcessor.text.toString())
                    addProperty("ram", binding.edtRamSize.text.toString())
                    addProperty("storage", binding.edtStorageSpace.text.toString())
                }
                updateSystemDetails(systemId, jsonObject)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@SystemDetailsActivity, "Please enter valid values", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }


    }

    private fun loadSystemDetails() {

        var bookMarkedSystems = dataManager.getBookmarkedSystems()
        val bookMarkedSystem = bookMarkedSystems.find { it.id == systemId }
        var isBookmarked = bookMarkedSystem != null
        if (isBookmarked) {
            binding.bookmarkBtn.setImageResource(R.drawable.bookmark_icon_filled)
        } else {
            binding.bookmarkBtn.setImageResource(R.drawable.bookmark_icon)
        }

        progressDialog.show()
        binding.systemIdTv.text = systemId.toString()
        Apis.SystemsApi.getSystem(systemId).enqueue(object : Callback<SystemInfo> {
            override fun onResponse(
                call: Call<SystemInfo>, response: retrofit2.Response<SystemInfo>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val systemDetails = response.body()
                    if (systemDetails != null) {
                        binding.labIdTv.text = systemDetails.lab_id.toString()
                        binding.switchWorking.isChecked = systemDetails.working
                        binding.edtDownloadSpeed.setText(if (systemDetails.download_speed == null) "0" else systemDetails.download_speed.toString())
                        binding.edtUploadSpeed.setText(if (systemDetails.upload_speed == null) "0" else systemDetails.upload_speed.toString())
                        binding.edtPingSpeed.setText(if (systemDetails.ping == null) "0" else systemDetails.ping.toString())
                        binding.edtSerialNo.setText(if (systemDetails.serial_no == null) "" else systemDetails.serial_no.toString())
                        binding.edtStorageSpace.setText(if (systemDetails.storage == null) "" else systemDetails.storage.toString())
                        binding.edtRamSize.setText(if (systemDetails.ram == null) "" else systemDetails.ram.toString())
                        binding.edtProcessor.setText(if (systemDetails.processor == null) "" else systemDetails.processor.toString())
                        binding.keyboardWorkingSwitch.isChecked = systemDetails.keyboard_working
                        binding.mouseWorkingSwitch.isChecked = systemDetails.mouse_working


                        binding.bookmarkBtn.setOnClickListener {
                            if (isBookmarked) {
                                dataManager.removeBookmarkSystem(bookMarkedSystem!!)
                                binding.bookmarkBtn.setImageResource(R.drawable.bookmark_icon)
                            } else {
                                dataManager.addBookmarkSystem(systemDetails)
                                binding.bookmarkBtn.setImageResource(R.drawable.bookmark_icon_filled)
                            }
                            bookMarkedSystems = dataManager.getBookmarkedSystems()
                            isBookmarked = bookMarkedSystems.find { it.id == systemId } != null
                        }
                        binding.backBtn.setOnClickListener {
                            finish()
                        }

                        dataManager.addPrevVisitedSystem(systemDetails)
                    }
                } else {
                    Toast.makeText(
                        this@SystemDetailsActivity,
                        RequestUtils.parseError(response),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SystemInfo>, t: Throwable) {
                t.printStackTrace()

                Toast.makeText(
                    this@SystemDetailsActivity, "Something went wrong", Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()
            }
        })

    }

    private fun updateSystemDetails(systemId: Int, jsonObject: JsonObject) {
        progressDialog.setMessage("Updating system details")
        progressDialog.show()

        Apis.SystemsApi.updateSystem(systemId, jsonObject,dataManager.authToken).enqueue(object : Callback<NormResponse> {
            override fun onResponse(p0: Call<NormResponse>, p1: retrofit2.Response<NormResponse>) {
                progressDialog.dismiss()
                if (p1.isSuccessful) {
                    val response = p1.body()
                    if (response != null) {
                        Toast.makeText(
                            this@SystemDetailsActivity, response.message, Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@SystemDetailsActivity, "Something went wrong", Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@SystemDetailsActivity, RequestUtils.parseError(p1), Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(p0: Call<NormResponse>, p1: Throwable) {
                p1.printStackTrace()
                progressDialog.dismiss()
                Toast.makeText(
                    this@SystemDetailsActivity, "Something went wrong", Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}