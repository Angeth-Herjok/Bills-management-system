package com.example.bills_management_system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bills_management_system.R
import com.example.bills_management_system.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_home)
    }
}