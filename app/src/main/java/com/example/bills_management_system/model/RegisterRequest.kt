package com.example.bills_management_system.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("first_name") var firstName: String,
    @SerializedName("last_name") var lastname: String,
    @SerializedName("phone_number")    var phoneNumber:String,
    var email: String,
    var password: String,
    var confirm:String,

    )
