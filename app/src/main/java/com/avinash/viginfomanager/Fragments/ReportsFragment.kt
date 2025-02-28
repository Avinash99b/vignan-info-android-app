package com.avinash.viginfomanager.Fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.avinash.viginfomanager.Adapters.ReportsLabsAdapter
import com.avinash.viginfomanager.Apis.Apis
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.Apis.RequestUtils
import com.avinash.viginfomanager.Apis.Responses.Block
import com.avinash.viginfomanager.Apis.Responses.Report
import com.avinash.viginfomanager.DashboardActivity
import com.avinash.viginfomanager.R
import com.avinash.viginfomanager.databinding.FragmentReportsBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call

class ReportsFragment : Fragment() {


    lateinit var binding: FragmentReportsBinding

    private val dataManager by lazy {
        DataManager.getInstance(activity as DashboardActivity)
    }

    private lateinit var selectedBlock: Block

    private var selectedFloors = ArrayList<Int>()

    private val progressDialog by lazy {
        ProgressDialog(activity as DashboardActivity).apply {
            setTitle("Please Wait")
            setCancelable(false)
        }
    }

    val reportsLabsAdapter by lazy {
        ReportsLabsAdapter(activity as DashboardActivity)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportsBinding.inflate(inflater, container, false)

        dataManager.blocks.observeForever {

            if (it == null) {
                return@observeForever
            }

            it.sortBy { it1 ->
                it1.id
            }
            binding.changeBlockIcon.setOnClickListener {
                val blocksCopy = dataManager.blocks.value
                if (blocksCopy == null) {
                    Toast.makeText(context, "Blocks Not Yet Loaded", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val blocks = ArrayList<Block>(blocksCopy)
                blocks.add(Block("All Blocks", 0, null, "All Blocks", 0))
                blocks.sortBy { it1 ->
                    it1.id
                }
                val builder =
                    AlertDialog.Builder(activity as DashboardActivity).setTitle("Chose A Block")

                val blockSelections = ArrayList<String>()

                for (i in 0 until blocks.size) {
                    blockSelections.add(blocks[i].id.toString() + " - " + blocks[i].name)
                }

                builder.setItems(blockSelections.toTypedArray()) { _, which ->
                    selectedBlock = blocks[which]
                    binding.blockNameTv.text = selectedBlock.name + " Block"
                    selectedFloors.clear()
                    refresh()
                }
                builder.show()
            }

            selectedBlock = it[0]
            refresh()
        }

        binding.labRecyclerLayout.layoutManager = LinearLayoutManager(activity)
        binding.labRecyclerLayout.adapter = reportsLabsAdapter

        return binding.root
    }

    private fun refresh() {
        val noOfFloors = selectedBlock.floors

        val chips = ArrayList<Chip>()

        for (i in 0 until noOfFloors) {
            val chip = Chip(activity as DashboardActivity)
            chip.setChipDrawable(
                ChipDrawable.createFromAttributes(
                    requireContext(), null, 0, R.style.FloorChipStyle
                )
            )
            chip.id = i + 1
            chip.text = "Floor ${i + 1}"
            chip.isClickable = true
            chip.isCheckable = true
            var exists = false
            selectedFloors.forEach {
                if (it == chip.id) {
                    exists = true
                }
            }
            chip.isChecked = exists
            chip.width = LinearLayout.LayoutParams.WRAP_CONTENT
            chip.height = LinearLayout.LayoutParams.MATCH_PARENT
            chip.chipBackgroundColor =
                ContextCompat.getColorStateList(requireContext(), R.color.chip_background)
            chip.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.chip_text))
            chips.add(chip)
        }

        binding.floorsSelectorGroup.removeAllViews()
        chips.forEach {
            binding.floorsSelectorGroup.addView(it)
        }

        binding.floorsSelectorGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            selectedFloors.clear()
            checkedIds.forEach {
                selectedFloors.add(it)
            }
            refresh()
        }


        progressDialog.show()
        Apis.ReportsApi.getDefaultReports(selectedBlock.id, JsonObject().apply {
            add("floors", JsonArray(selectedFloors.size).apply {
                selectedFloors.forEach {
                    add(it)
                }
            })
        }).enqueue(object : retrofit2.Callback<Report> {
            override fun onResponse(
                call: Call<Report>, response: retrofit2.Response<Report>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val report = response.body()
                    if (report != null) {
                        binding.totalSystemsTv.text = report.totalCount
                        binding.workingSystemsTv.text = report.workingCount

                        val workingPercentage =
                            report.workingCount.toFloat() / report.totalCount.toFloat() * 100
                        binding.workingRatioInfoTv.text =
                            String.format("%.2f", workingPercentage) + "% Working"

                        binding.notWorkingSystemsTv.text =
                            (report.totalCount.toInt() - report.workingCount.toInt()).toString()
                        binding.notWorkingRatioInfoTv.text =
                            (100 - workingPercentage).toString() + "% Not Working"
                    } else {
                        Toast.makeText(context, "No Reports Found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, RequestUtils.parseError(response), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(p0: Call<Report>, p1: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(context, p1.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}