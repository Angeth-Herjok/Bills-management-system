package com.example.bills_management_system.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose var message: String,
    @Expose @SerializedName ("user_id")var userId: String,
    @Expose @SerializedName("accessToken") var accessToken: String,

    )




