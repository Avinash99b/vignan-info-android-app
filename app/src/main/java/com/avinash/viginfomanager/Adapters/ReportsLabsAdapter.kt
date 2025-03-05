package com.avinash.viginfomanager.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avinash.viginfomanager.Apis.Responses.Lab
import com.avinash.viginfomanager.SystemSelectorDialog
import com.avinash.viginfomanager.databinding.RecyclerLabLayoutBinding
import com.avinash.viginfomanager.databinding.RecyclerLayoutReportLabBinding

class ReportsLabsAdapter(val activity:Activity): RecyclerView.Adapter<ReportsLabsAdapter.ViewHolder>() {

    private val systemSelectorDialog = SystemSelectorDialog(activity)

    var labs = ArrayList<Lab>()
        set(value) = run {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerLayoutReportLabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return labs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lab = labs[position]
        holder.binding.root.setOnClickListener {
            systemSelectorDialog.show(lab.id)
        }
    }
    class ViewHolder(val binding:RecyclerLayoutReportLabBinding): RecyclerView.ViewHolder(binding.root)
}