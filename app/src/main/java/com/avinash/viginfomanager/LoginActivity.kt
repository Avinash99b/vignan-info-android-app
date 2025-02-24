package com.avinash.viginfomanager

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.avinash.viginfomanager.Apis.Apis
import com.avinash.viginfomanager.Apis.RequestUtils
import com.avinash.viginfomanager.Apis.Responses.AuthToken
import com.avinash.viginfomanager.databinding.ActivityLoginBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)

        binding.continueButton.setOnClickListener {
            val registerNo = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            // Validate the input fields
            if (registerNo.isEmpty()) {
                binding.emailInput.error = "Please enter your register number"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordInput.error = "Please enter your password"
                return@setOnClickListener
            }

            if (registerNo.length != 10) {
                binding.emailInput.error = "Please enter a valid register number"
                return@setOnClickListener
            }

            // Convert all regex into single regex
            if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=]).{8,}\$".toRegex())) {
                binding.passwordInput.error =
                    "Password should be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number and one special character"
                return@setOnClickListener
            }

            progressDialog.show()
            login(registerNo, password)
        }

        binding.googleSignInButton.setOnClickListener {
            // Handle Google Sign In
            Toast.makeText(this, "Feature Implemented Soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(registerNo: String, password: String) {
        // Handle login

        val jsonObject = JsonObject()
        jsonObject.apply {
            addProperty("registerNo", registerNo)
            addProperty("password", password)
        }
        Apis.UsersApi.login(jsonObject).enqueue(object : retrofit2.Callback<AuthToken> {
            override fun onResponse(call: Call<AuthToken>, response: Response<AuthToken>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val authToken = response.body()
                    if (authToken != null) {
                        val editor = getSharedPreferences("vig", MODE_PRIVATE).edit()
                        editor.putString("token", authToken.authToken)
                        editor.commit()

                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        return
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        RequestUtils.parseError(response),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AuthToken>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}