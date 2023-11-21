package com.example.bills_management_system.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bills_management_system.databinding.ActivityLogoutBinding
import com.example.bills_management_system.utils.Constants

class LogoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            logOut()
        }
    }
    fun logOut(){
        val sharedPrefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.remove(Constants.USER_ID)
        editor.remove(Constants.ACCESS_TOKEN)
        editor.apply()

        val intent =Intent(this, Login::class.java)
        startActivity(intent)
        finish()

    }
}



