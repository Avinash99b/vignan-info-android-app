package com.avinash.viginfomanager.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avinash.viginfomanager.Apis.Responses.Lab
import com.avinash.viginfomanager.SystemSelectorDialog
import com.avinash.viginfomanager.databinding.RecyclerLabLayoutBinding

class LabsAdapter(val activity:Activity): RecyclerView.Adapter<LabsAdapter.ViewHolder>() {

    private val systemSelectorDialog = SystemSelectorDialog(activity)

    var labs = ArrayList<Lab>()
        set(value) = run {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerLabLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return labs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lab = labs[position]
        holder.binding.labIdTv.text = "ID:- ${lab.id}"
        holder.binding.labNameTv.text = "Name:- ${lab.name}"
        holder.binding.labFloorTv.text = "Floor:- ${lab.floor}"
        holder.binding.labInchargeTv.text = "Incharge:- ${lab.incharge_id}"
        holder.binding.labDesTv.text = lab.description

        holder.binding.root.setOnClickListener {
            systemSelectorDialog.show(lab.id)
        }
    }
    class ViewHolder(val binding:RecyclerLabLayoutBinding): RecyclerView.ViewHolder(binding.root)
}