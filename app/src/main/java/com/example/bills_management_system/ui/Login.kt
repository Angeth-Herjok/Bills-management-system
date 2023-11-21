package com.example.bills_management_system.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.bills_management_system.model.LoginRequest
import com.example.bills_management_system.model.RegisterResponse
import com.example.bills_management_system.viewmodel.UserViewModel
import com.example.bills_management_system.databinding.ActivityLoginBinding


class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var ivBack : ActivityLoginBinding
    lateinit var btnLogin: ActivityLoginBinding
    lateinit var btnSignUp: ActivityLoginBinding

    val UserViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        validate()
        binding.btnLogin.setOnClickListener {
            validate()
        }
        binding.ivBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        UserViewModel.errLiveData.observe(this, Observer { error->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            binding.pbProgressBar.visibility = View.GONE
        })

        UserViewModel.logLiveData.observe(this, Observer { logResponse ->
            persistLogIn(logResponse)
            binding.pbProgressBar.visibility = View.GONE
            Toast.makeText(this, logResponse.message, Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeActivity:: class.java))
            finish()
        })
    }


    fun validate() {
        clearError()

        val email = binding.etEmail.text.toString()
        val password=binding.etPassword.text.toString()
        var error = false

        if (email.isEmpty()){
            error= true
            binding.tilEmail.error = "Email Address is Required"

        }
        if (password.isBlank()){
            error= true
            binding.tilPassword.error = "Wrong password"
        }

        if (!error) {
            val loginRequest = LoginRequest(
                email = email,
                password = password

            )
            binding.pbProgressBar.visibility = View.VISIBLE
            UserViewModel.loginUser(loginRequest)

        }
    }
    fun clearError() {
        binding.tilEmail.error = null
        binding.tilPassword.error = null

    }


    fun persistLogIn(loginResponse: RegisterResponse){
        val sharedPrefs = getSharedPreferences("BILLZ_PREFS", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("USER_ID", loginResponse.userId)
        editor.putString("ACCESS_TOKEN", loginResponse.accessToken)
        editor.apply()
    }
}





