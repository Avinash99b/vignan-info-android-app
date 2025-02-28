package com.avinash.viginfomanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.avinash.viginfomanager.Adapters.DashboardVPAdapter
import com.avinash.viginfomanager.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.viewPager.adapter = DashboardVPAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> binding.viewPager.currentItem = 0
                R.id.add -> binding.viewPager.currentItem = 1
                R.id.reports -> binding.viewPager.currentItem = 2
                R.id.profile -> binding.viewPager.currentItem = 3
            }
            true
        }
    }

    fun switchToHome(){
        binding.viewPager.currentItem = 0
        binding.bottomNavigationView.selectedItemId = R.id.home
    }
}