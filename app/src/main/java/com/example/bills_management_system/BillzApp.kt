package com.example.bills_management_system

import android.app.Application
import android.content.Context
import androidx.work.*
import com.example.bills_management_system.WorkManager.DataSyncWorker
import com.example.bills_management_system.WorkManager.UpcomingBillsWorker
import com.example.bills_management_system.utils.Constants
import java.util.concurrent.TimeUnit

class BillzApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        val periodicWorkRequest = PeriodicWorkRequestBuilder<UpcomingBillsWorker>(
            15, TimeUnit.MINUTES
        ).addTag(Constants.CREATE_RECURRING_BILLS).build()
        WorkManager
            .getInstance(appContext)
            .enqueueUniquePeriodicWork(
                Constants.CREATE_RECURRING_BILLS,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val syncPeriodicWorkRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
            15, TimeUnit.MINUTES
        )
            .addTag(Constants.SYNC_BILLS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(appContext)
            .enqueueUniquePeriodicWork(
                Constants.SYNC_BILLS,
                ExistingPeriodicWorkPolicy.KEEP,
                syncPeriodicWorkRequest
            )
    }
}
