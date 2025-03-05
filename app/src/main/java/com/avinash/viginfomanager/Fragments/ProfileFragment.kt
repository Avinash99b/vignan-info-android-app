package com.avinash.viginfomanager.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.avinash.viginfomanager.LoginActivity
import com.avinash.viginfomanager.R
import com.avinash.viginfomanager.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.logout.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    context?.getSharedPreferences("vig", Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
                    activity?.startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
        }
        return binding.root;
    }
}

