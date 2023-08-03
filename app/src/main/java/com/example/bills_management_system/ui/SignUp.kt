package com.example.bills_management_system.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bills_management_system.R
import com.example.bills_management_system.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    lateinit var binding:ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sign_up)
    }
    override fun onResume() {
        super.onResume()
        setContentView(binding.root)
        binding.btnSignUP.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        fun validateSignUp() {
            binding.tilEmail.error = null
            binding.tilPassword.error = null
            binding.tilPhone.error = null
            binding.tilUserName.error = null


            val phone = binding.etPhone.text.toString()
            val lastName = binding.etPassword.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val userName = binding.etUserName.text.toString()
            var error = false

            if (userName.isBlank()) {
                binding.tilUserName.error = "Valid email required"
            }
            if (lastName.isBlank()) {
                binding.tilEmail.error = "first name required"
                error=true
            }
            if (email.isBlank()) {
                binding.tilEmail.error = "first name required"
                error=true
            }
            if (password.isBlank()) {
                binding.tilPassword.error = "Correct password required"
                error = true
            }
            if (phone.isBlank()) {
                binding.tilPhone.error = "Correct phone required"
                error = true
            }
            if (!error) {
                Toast.makeText(this, "$email $password", Toast.LENGTH_LONG).show()
            }
        }

    }
}