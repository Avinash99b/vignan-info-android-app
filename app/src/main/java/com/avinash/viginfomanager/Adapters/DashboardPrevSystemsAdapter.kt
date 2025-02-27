package com.avinash.viginfomanager.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.avinash.viginfomanager.Apis.Apis
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.Apis.RequestUtils
import com.avinash.viginfomanager.databinding.RecyclerLayoutPrevVisitedSystemsBinding
import com.avinash.viginfomanager.Apis.Responses.SystemInfo
import com.avinash.viginfomanager.R
import com.avinash.viginfomanager.SystemDetailsActivity

class DashboardPrevSystemsAdapter(val activity:Activity): RecyclerView.Adapter<DashboardPrevSystemsAdapter.ViewHolder>() {

    private val dataManager = DataManager.getInstance(activity)

    private var prevVisitedSystems = dataManager.prevVisitedSystems.value

    init {
        dataManager.prevVisitedSystems.observeForever {
            prevVisitedSystems = it
            notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerLayoutPrevVisitedSystemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataManager.prevVisitedSystems.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(prevVisitedSystems == null) return
        val system = prevVisitedSystems?.get(position) ?: return

        holder.binding.loadingIndicator.visibility = android.view.View.VISIBLE
        holder.binding.root.setOnClickListener{
            activity.startActivity(Intent(activity,SystemDetailsActivity::class.java).apply {
                putExtra("systemId", system.id)
            })
        }
        Apis.SystemsApi.getSystem(system.id).enqueue(object : retrofit2.Callback<SystemInfo> {
            override fun onResponse(call: retrofit2.Call<SystemInfo>, response: retrofit2.Response<SystemInfo>) {
                holder.binding.loadingIndicator.visibility = android.view.View.GONE
                holder.binding.systemStatusImage.visibility = android.view.View.VISIBLE
                val reqSystem = response.body()
                if(response.isSuccessful && reqSystem != null){
                    if(reqSystem.working){
                        holder.binding.systemStatusImage.setImageResource(R.raw.working)
                    }else{
                        holder.binding.systemStatusImage.setImageResource(R.raw.not_working)
                    }
                    holder.binding.blockIdTv.text = system.block_id.toString()
                    holder.binding.floorNameTv.text = "-F${system.floor}-"
                    holder.binding.roomNameTv.text = system.name

                }else{
                    Toast.makeText(holder.binding.root.context, RequestUtils.parseError(response), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<SystemInfo>, t: Throwable) {
                Toast.makeText(holder.binding.root.context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    class ViewHolder(val binding:RecyclerLayoutPrevVisitedSystemsBinding): RecyclerView.ViewHolder(binding.root)
}