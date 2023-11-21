package com.example.bills_management_system.model


data class BillsSummary(
    val paid: Double,
    val upcoming: Double,
    val overdue: Double,
    val total: Double
)