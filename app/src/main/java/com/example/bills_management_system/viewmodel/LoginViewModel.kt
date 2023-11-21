//package com.example.bills_management_system.viewmodel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.bills_management_system.model.LoginRequest
//import com.example.bills_management_system.model.LoginResponse
//import com.example.bills_management_system.model.RegisterResponse
//import com.example.bills_management_system.repository.UserRepository
//import kotlinx.coroutines.launch
//
//class LoginViewModel: ViewModel(){
//    val userRepo = UserRepository()
//    val regLiveData = MutableLiveData<RegisterResponse>()
//    var errorLiveData = MutableLiveData<Throwable>()
//
//
//    fun loginUser(loginRequest: LoginRequest) {
//        viewModelScope.launch {
//            val response = userRepo.registerUser(loginRequest)
//            if (response.isSuccessful) {
//                regLiveData.postValue(response.body())
//            } else {
//                errorLiveData.postValue(response.errorBody()?.string()?.
//                let { Throwable(it) })
//            }
//        }
//    }
//}
//
//private fun <T> MutableLiveData<T>.postValue(body: LoginResponse?) {
//    TODO("Not yet implemented")
//}


