package com.example.bills_management_system.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bills_management_system.R
import com.example.bills_management_system.databinding.ActivityLoginBinding
import com.example.bills_management_system.HomeActivity

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
//        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            val intent=Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }
        validateRegistration()
        clearErrors()
    }

    fun validateRegistration() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        var error = false
        if (email.isBlank()) {
            binding.tilEmail.error = "Email required"
            error = true
        }
        if (password.isBlank()) {
            binding.tilPassword.error = "Password required"
            error = true
        }
        if (!error) {
            Toast.makeText(
                this, "$email",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun clearErrors() {
        binding.tilEmail.error = null
    }
}


