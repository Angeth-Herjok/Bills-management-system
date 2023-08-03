package com.example.bills_management_system.repository

import com.example.bills_management_system.api.ApiClient
import com.example.bills_management_system.api.ApiInterface
import com.example.bills_management_system.model.LoginRequest
import com.example.bills_management_system.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository {
    val apiClient = ApiClient.buildClient(ApiInterface::class.java)
    suspend fun registerUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            apiClient.loginUser(loginRequest)
        }
    }
}