package com.example.bills_management_system.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    var message:String,
    @SerializedName("user_id")
    var user:User,
)
