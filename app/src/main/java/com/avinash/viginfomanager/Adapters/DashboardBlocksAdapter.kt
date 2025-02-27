package com.avinash.viginfomanager.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.LabSelectorDialog
import com.avinash.viginfomanager.databinding.RecyclerLayoutHomeBlockBinding

class DashboardBlocksAdapter(activity: Activity) :
    RecyclerView.Adapter<DashboardBlocksAdapter.ViewHolder>() {

    private val dataManager = DataManager.getInstance(activity)

    private val labSelectorDialog by lazy {
        LabSelectorDialog(activity)
    }

    init {
        dataManager.blocks.observeForever {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerLayoutHomeBlockBinding.inflate(LayoutInflater.from(parent.context), parent,
                false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataManager.blocks.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val block = dataManager.blocks.value?.get(position) ?: return
        holder.binding.blockNameTv.text = "${block.id}. ${block.name}"
        holder.binding.blockDesTv.text = block.description

        holder.binding.root.setOnClickListener {
            labSelectorDialog.show(block.id)
        }
    }

    class ViewHolder(val binding: RecyclerLayoutHomeBlockBinding) :
        RecyclerView.ViewHolder(binding.root)
}