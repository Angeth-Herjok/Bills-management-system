package com.example.bills_management_system.utils

import java.text.DecimalFormat

class Utils {
    companion object{
        fun formatCurrency(amount: Double):String{
            val fmt = DecimalFormat("KES #,###")
            return fmt.format(amount)
        }
    }
}


