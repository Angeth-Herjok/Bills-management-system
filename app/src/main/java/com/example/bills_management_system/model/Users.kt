package com.example.bills_management_system.model

import com.google.gson.annotations.Expose

data class Users(
    @Expose var email:String,
    @Expose var password:String,
)
