package com.example.bills_management_system.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bills_management_system.R
import com.example.bills_management_system.databinding.ActivityMainBinding
import com.example.bills_management_system.utils.Constants

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
        }, 2000)

    }

    fun redirectUser(){
        val sharedPrefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        val userId = sharedPrefs.getString(Constants.USER_ID, Constants.EMPTY_STRING)
        if (userId.isNullOrBlank()){
            startActivity(Intent(this, Login::class.java))
        }
        else{
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}