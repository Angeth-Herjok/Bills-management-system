package com.example.bills_management_system.ui

//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.os.CountDownTimer
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.fragment.app.viewModels
//import com.anychart.BuildConfig
//import com.example.bills_management_system.R
//import com.example.bills_management_system.databinding.FragmentSettingBillzBinding
//import com.example.bills_management_system.utils.Constants
//import com.example.bills_management_system.viewmodel.BillsViewModel
//
//
//import com.google.android.material.snackbar.Snackbar
//
//class SettingFragment : Fragment() {
//    var binding: FragmentSettingBillzBinding? =null
//    val billViewModel: BillsViewModel by viewModels()
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.fragment_setting_billz, container, false)
//        val logout = view.findViewById<ImageView>(R.id.tvlogout)
//        logout.setOnClickListener {
//            performLogout()
//        }
//        return view
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onResume() {
//        super.onResume()
//
//        binding?.tvAppVersion?.text= "APP VERSION: ${com.example.bills_management_system.BuildConfig.VERSION_NAME}"
//        binding?.tvlogout?.setOnClickListener{ performLogout()}
//        binding?.tvsync?.setOnClickListener{ syncData()}
//
//
//    }
//
//    private fun performLogout() {
//        val sharedPrefs = requireActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
//
//        var editor = sharedPrefs.edit()
//        editor= sharedPrefs.edit()
//        editor.putString(Constants.USER_ID, Constants.EMPTY_STRING)
//        editor.putString(Constants.ACCESS_TOKEN, Constants.EMPTY_STRING)
//        editor.apply()
//
//        val intent = Intent(requireContext(), Login::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        requireActivity().finish()
//        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
//
//    }
//
//    fun syncData(){
////        Detect Network Connection(proceed where there is network connection)
//        binding?.pbSync?.visibility= View.VISIBLE
//        billViewModel.fetchRemoteBills()
//
//        val timer= object : CountDownTimer(1000,100){
//            override fun onTick(p0: Long) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onFinish() {
//                binding?.pbSync
//                Snackbar.make(binding!!.settingsRoot,"Sync Completed",Snackbar.LENGTH_LONG).show()
//            }
//        }
//        timer.start()
//    }
//
//    override fun onDestroy(){
//        super.onDestroy()
//        binding = null
//    }
//}


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.bills_management_system.R
import com.example.bills_management_system.databinding.FragmentSettingBillzBinding
import com.example.bills_management_system.utils.Constants
import com.example.bills_management_system.viewmodel.BillsViewModel
import com.google.android.material.snackbar.Snackbar

class SettingFragment : Fragment() {
    var binding: FragmentSettingBillzBinding? = null
    val billViewModel: BillsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting_billz, container, false)
        val logout = view.findViewById<ImageView>(R.id.tvlogout)
        logout.setOnClickListener {
            performLogout()
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        val BuildConfig = null
        val also =
            "APP VERSION: ${BuildConfig.VERSION_NAME}".also { binding?.tvAppVersion?.text = it }
        binding?.tvlogout?.setOnClickListener { performLogout() }
        binding?.tvsync?.setOnClickListener { syncData() }
    }

    private fun performLogout() {
        val sharedPrefs = requireActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)

        var editor = sharedPrefs.edit()
        editor = sharedPrefs.edit()
        editor.putString(Constants.USER_ID, Constants.EMPTY_STRING)
        editor.putString(Constants.ACCESS_TOKEN, Constants.EMPTY_STRING)
        editor.apply()

        val intent = Intent(requireContext(), Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    fun syncData() {
        // Detect Network Connection (proceed where there is a network connection)
        binding?.pbSync?.visibility = View.VISIBLE
        billViewModel.fetchRemoteBills()

        val timer = object : CountDownTimer(1000, 100) {
            override fun onTick(p0: Long) {
                // TODO: Implement onTick if needed
            }

            override fun onFinish() {
                binding?.pbSync?.visibility = View.GONE
                Snackbar.make(binding!!.settingsRoot, "Sync Completed", Snackbar.LENGTH_LONG).show()
            }
        }
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
