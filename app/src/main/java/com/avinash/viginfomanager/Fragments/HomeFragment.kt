package com.avinash.viginfomanager.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.avinash.viginfomanager.Adapters.DashboardBlocksAdapter
import com.avinash.viginfomanager.Adapters.DashboardPrevSystemsAdapter
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.R
import com.avinash.viginfomanager.databinding.ActivityLoginBinding
import com.avinash.viginfomanager.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.blocksRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.blocksRecyclerView.adapter = DashboardBlocksAdapter(requireActivity())

        dataManager = DataManager.getInstance(requireActivity())

        binding.searchView.setEndIconOnClickListener {
            binding.searchView.editText?.text?.clear()
        }


        binding.visitedSystemsRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.visitedSystemsRecycler.adapter = DashboardPrevSystemsAdapter(requireActivity())

        return binding.root;
    }
}