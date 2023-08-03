package com.example.bills_management_system.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bills_management_system.model.RegisterRequest
import com.example.bills_management_system.model.RegisterResponse
import com.example.bills_management_system.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel:ViewModel() {
    private  val userRepo = UserRepository()
    val regLiveData = MutableLiveData<RegisterResponse>()
    var errLiveData = MutableLiveData<String>()

    fun registerUser(registerRequest: RegisterRequest) {

        viewModelScope.launch {


        }
    }

}