package com.example.bills_management_system.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @Expose var message:String,
    @SerializedName("user_id")
    @Expose var user:User,
)

