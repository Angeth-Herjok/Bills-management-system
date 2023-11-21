package com.example.bills_management_system.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bills_management_system.model.LoginRequest
import com.example.bills_management_system.model.LoginResponse
import com.example.bills_management_system.model.RegisterRequest
import com.example.bills_management_system.model.RegisterResponse
import com.example.bills_management_system.repository.UserRepository
import kotlinx.coroutines.launch


class UserViewModel : ViewModel() {
    val userRepo = UserRepository()
    val regiLiveData = MutableLiveData<RegisterResponse>()
    val logLiveData = MutableLiveData<LoginResponse>()
    val errLiveData = MutableLiveData<String>()

    fun registerUser(registerRequest: RegisterRequest){
        viewModelScope.launch {
            val response = userRepo.registerUser(registerRequest)
            if (response.isSuccessful){
                regiLiveData.postValue(response.body())
            }
            else{
                errLiveData.postValue(response.errorBody()?.string())
            }
        }
    }


    fun loginUser(loginRequest: LoginRequest){
        viewModelScope.launch {
            val response = userRepo.loginUser(loginRequest)
            if (response.isSuccessful){
                logLiveData.postValue(response.body())
            }
            else{
                errLiveData.postValue(response.errorBody()?.string())
            }
        }
    }


}