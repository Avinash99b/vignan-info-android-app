package com.avinash.viginfomanager.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.avinash.viginfomanager.DashboardActivity
import com.avinash.viginfomanager.Fragments.AddFragment
import com.avinash.viginfomanager.Fragments.HomeFragment
import com.avinash.viginfomanager.Fragments.ProfileFragment
import com.avinash.viginfomanager.Fragments.ReportsFragment

class DashboardVPAdapter(activity: DashboardActivity):FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> AddFragment()
            2 -> ReportsFragment()
            3 -> ProfileFragment()
            else -> HomeFragment()
        }
    }
}