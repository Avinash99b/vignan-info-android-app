package com.avinash.viginfomanager.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avinash.viginfomanager.Apis.Responses.System
import com.avinash.viginfomanager.R
import com.avinash.viginfomanager.SystemDetailsActivity
import com.avinash.viginfomanager.databinding.RecyclerSystemLayoutBinding
import java.lang.reflect.Array.set

class SystemsAdapter(val activity: Activity) : RecyclerView.Adapter<SystemsAdapter.ViewHolder>() {

    var systems = ArrayList<System>()
        set(value) = run {
            var sortedList = value.sortedBy { it.id }
            sortedList = sortedList.sortedBy { it.working }

            field = ArrayList(sortedList)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerSystemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return systems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val system = systems[position]
        holder.binding.sysIdTv.text = "ID:- ${system.id}"
        if(system.working){
            holder.binding.statusView.setImageResource(R.raw.working)
        }else{
            holder.binding.statusView.setImageResource(R.raw.not_working)
        }

        holder.binding.root.setOnClickListener {
            activity.startActivity(Intent(activity, SystemDetailsActivity::class.java).apply {
                putExtra("systemId", system.id)
            });
        }
    }

    class ViewHolder(val binding: RecyclerSystemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}