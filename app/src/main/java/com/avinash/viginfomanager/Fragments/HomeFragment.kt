package com.avinash.viginfomanager.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.avinash.viginfomanager.Adapters.DashboardBlocksAdapter
import com.avinash.viginfomanager.Adapters.DashboardSystemsAdapter
import com.avinash.viginfomanager.Apis.DataManager
import com.avinash.viginfomanager.R
import com.avinash.viginfomanager.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    lateinit var dataManager: DataManager

    private val dashboardSystemsAdapter by lazy { DashboardSystemsAdapter(requireActivity()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.blocksRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.blocksRecyclerView.adapter = DashboardBlocksAdapter(requireActivity())

        dataManager = DataManager.getInstance(requireActivity())

        binding.searchView.setEndIconOnClickListener {
            binding.searchView.editText?.text?.clear()
        }


        binding.visitedSystemsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.visitedSystemsRecycler.adapter = dashboardSystemsAdapter


        binding.systemsModeSelector.setOnCheckedChangeListener { _, checkedId ->
            dataManager.dashboardSystemsMode.value = when (checkedId) {
                R.id.history_chip -> DataManager.DashboardSystemShowMode.PREV_VISITED
                R.id.bookmarks_chip -> DataManager.DashboardSystemShowMode.BOOKMARKED
                else -> DataManager.DashboardSystemShowMode.PREV_VISITED
            }
        }

        dataManager.dashboardSystemsMode.observe(viewLifecycleOwner) {
            binding.systemsModeSelector.check(
                when (it) {
                    DataManager.DashboardSystemShowMode.PREV_VISITED -> R.id.history_chip
                    DataManager.DashboardSystemShowMode.BOOKMARKED -> R.id.bookmarks_chip
                    else -> R.id.history_chip
                }
            )

            dataManager.refreshSystems()
        }

        return binding.root;
    }
}