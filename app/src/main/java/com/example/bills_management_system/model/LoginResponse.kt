package com.example.bills_management_system.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    var message:String,
    @SerializedName("accessToken") var accssToken: String,
    @SerializedName("user_id") var user:User,
)
