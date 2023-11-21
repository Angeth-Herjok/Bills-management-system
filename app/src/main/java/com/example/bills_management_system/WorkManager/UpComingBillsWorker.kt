package com.example.bills_management_system.WorkManager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bills_management_system.repository.BillRepository

class UpcomingBillsWorker(context: Context, workerParams: WorkerParameters) :CoroutineWorker(context, workerParams){
    val billRepository = BillRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        billRepository.createRecurringWeeklyBills()
        billRepository.createRecurringMonthlyBills()
//        billRepository.createRecurringQuarterlyBills()
        billRepository.createRecurringAnnualBills()

        return Result.success()
    }
}