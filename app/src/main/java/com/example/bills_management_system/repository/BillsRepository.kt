package com.example.bills_management_system.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bills_management_system.BillzApp
import com.example.bills_management_system.Database.BillzDb
import com.example.bills_management_system.api.ApiClient
import com.example.bills_management_system.api.ApiInterface
import com.example.bills_management_system.model.Bill
import com.example.bills_management_system.model.BillsSummary
import com.example.bills_management_system.model.UpcomingBill
import com.example.bills_management_system.utils.Constants
import com.example.bills_management_system.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BillRepository {
    val database= BillzDb.getDatabase(BillzApp.appContext)
    val BillDao = database.billDao()
    val upcomingBillDao=database.upcomingBillsDao()
    val apiClient = ApiClient.buildClient(ApiInterface::class.java)

    suspend fun saveBill(bill: Bill){
        withContext(Dispatchers.IO){
            BillDao.insertBill(bill)
        }
    }
//    fun getAllBills(): LiveData<List<Bill>> {
//        return BillDao.getAllBills()
//    }

    suspend fun insertUpcomingBill(upcomingBill: UpcomingBill){
        withContext(Dispatchers.IO){
            upcomingBillDao.insertUpcomingBill(upcomingBill)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createRecurringMonthlyBills(){
        withContext(Dispatchers.IO){
            val monthlyBills=BillDao.getRecurringBills(Constants.MONTHLY)
            val startDate= DateTimeUtils.getFirstDayOfMonth()
            val endDate= DateTimeUtils.getLastDayOfMonth()
            val year=DateTimeUtils.getCurrentYear()
            val month=DateTimeUtils.getCurrentMonth()
            monthlyBills.forEach { bill->
                val existing=upcomingBillDao.queryExistingBill(bill.billId,startDate, endDate)
                if (existing.isEmpty()){
                    val newUpcomingBill= UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId=bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = DateTimeUtils.createDateFromDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false,
                        synced = false

                    )
                    upcomingBillDao.insertUpcomingBill(newUpcomingBill)

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createRecurringWeeklyBills(){
        withContext(Dispatchers.IO){
            val weeklyBills=BillDao.getRecurringBills(Constants.WEEKLY)
            val startDate= DateTimeUtils.getFirstDayOfWeek()
            val endDate= DateTimeUtils.getLastDateOfWeek()
            weeklyBills.forEach { bill->
                val existingBill=upcomingBillDao.queryExistingBill(bill.billId,startDate, endDate)
                if (existingBill.isEmpty()){
                    val newWeeklyBills= UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId=bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = DateTimeUtils.getDateOfWeekDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false,
                        synced = false
                    )
                    upcomingBillDao.insertUpcomingBill(newWeeklyBills)
                }
            }


        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createRecurringAnnualBills(){
        withContext(Dispatchers.IO){
            val annualBills = BillDao.getRecurringBills(Constants.ANNUAL)
            val currentYear = DateTimeUtils.getCurrentYear()
            val startDate = "$currentYear-01-01"
            val endDate = "$currentYear-12-31"
            annualBills.forEach { bill ->
                val existing = upcomingBillDao.queryExistingBill(bill.billId, startDate,endDate)
                if (existing.isEmpty()){
                    val newWeeklyBills= UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId=bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = "$currentYear-${bill.dueDate}",
                        userId = bill.userId,
                        paid = false,
                        synced = false
                    )
                    upcomingBillDao.insertUpcomingBill(newWeeklyBills)

                }
            }
        }
    }



    fun getAuthToken(): String{
        val prefs = BillzApp.appContext
            .getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        var token = prefs.getString(Constants.ACCESS_TOKEN, Constants.EMPTY_STRING)
        token ="Bearer $token"
        return token
    }

    fun getUpcomingBillsByFrequency(freq: String):LiveData<List<UpcomingBill>>{
        return upcomingBillDao.getUpcomingBillsByFrequency(freq , false)
    }

    suspend fun updateUpcomingBill(upcomingBill: UpcomingBill){
        withContext(Dispatchers.IO){
            upcomingBillDao.updateUpcomingBill(upcomingBill)
        }
    }
    fun getPaidBills(): LiveData<List<UpcomingBill>>{
        return upcomingBillDao.getPaidBills()
    }

    suspend fun synchedBills(){
        withContext(Dispatchers.IO){

            var token = getAuthToken()
            val unsynchedBills = BillDao.getUnsyncedBills()
            unsynchedBills.forEach { bill ->
                val response= apiClient.postBill(token, bill)

                if (response.isSuccessful){
                    bill.synced = true
                    BillDao.saveBill(bill)
                }
            }
        }
    }
    suspend fun synchedUpcomingBills(){
        withContext(Dispatchers.IO){
            var token = getAuthToken()
            upcomingBillDao.getUnsyncedUpcomingBills().forEach { UpcomingBill ->
                val response = apiClient.postUpcomingBill(token,UpcomingBill)

                if (response.isSuccessful){
                    UpcomingBill.synced = true
                    upcomingBillDao.updateUpcomingBill(UpcomingBill)
                }
            }
        }
    }

    suspend fun fetchRemoteBills(){
        withContext(Dispatchers.IO){
            val response = apiClient.fetchRemoteBills(getAuthToken())
            if (response.isSuccessful){
                response.body()?.forEach { bill ->
                    bill.synced=true
                    BillDao.saveBill(bill) }
            }
        }
    }

    suspend fun fetchRemoteUpcomingBills(){
        withContext(Dispatchers.IO){
            val response = apiClient.fetchRemoteUpcomingBills(getAuthToken())
            if (response.isSuccessful){
                response.body()?.forEach { UpcomingBill ->
                    UpcomingBill.synced=true
                    upcomingBillDao.insertUpcomingBill(UpcomingBill) }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getMonthlySummary():LiveData<BillsSummary>{
        return withContext(Dispatchers.IO){
            val startDate = DateTimeUtils.getFirstDayOfMonth()
            val endDate = DateTimeUtils.getLastDayOfMonth()
            val today = DateTimeUtils.getDateToday()
            val paid= upcomingBillDao.getPaidMonthlyBillsSum(startDate, endDate)
            val upcoming= upcomingBillDao.getUpcomingBillsThisMonth(startDate, endDate, today)
            val total = upcomingBillDao.getTotalMonthlyBills(startDate, endDate)
            val overdue = upcomingBillDao.getOverdueBillsThisMonth(startDate,endDate,today)
            val summary= BillsSummary(paid=paid, overdue= overdue, upcoming = upcoming, total = total)
            MutableLiveData(summary)
        }
    }
}

//private fun Any.insertBill(bill: Bill) {
//
//}

//import android.content.Context
//import com.example.bills_management_system.BillzApp
//import com.example.bills_management_system.Database.BillzDb
//import com.example.bills_management_system.model.Bill
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.example.bills_management_system.Database.BillDao
//import com.example.bills_management_system.api.ApiClient
//import com.example.bills_management_system.api.ApiInterface
//import com.example.bills_management_system.model.BillsSummary
//import com.example.bills_management_system.model.UpcomingBill
//import com.example.bills_management_system.utils.Constants
//import com.example.bills_management_system.utils.DateTimeUtils
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import java.util.Calendar
//import java.util.UUID
//
//class BillsRepository{
//    val db = BillzDb.getDatabase(BillzApp.appContext)
//    val billDao=db.billDao()
//    val upcomingBillsDao = db.upcomingBillsDao()
//    val apiClient = ApiClient.buildClient((ApiInterface::class.java))
//
//    suspend fun saveBill(bill: Bill){
//        withContext(Dispatchers.IO){
//            BillDao.insertBill(bill)
//        }
//    }
//    fun getAllBills(): LiveData<List<Bill>> {
//        return billDao.getAllBills()
//    }
//
//
//
//    suspend fun insertUpcomingBill(upcomingBill: UpcomingBill){
//        withContext(Dispatchers.IO){
//            upcomingBillsDao.insertUpcomingBill(upcomingBill)
//        }
//    }
//    suspend fun createRecurringMonthlyBills(){
//        withContext(Dispatchers.IO){
//            val montlyBills = billDao.getRecurringBills(Constants.MONTHLY)
//            montlyBills.forEach{bill ->
//                val cal = Calendar.getInstance()
//                val month = cal.get(Calendar.MONTH)+1
//                var monthstr= month.toString()
//                if (month<10){
//                    monthstr="0$month"
//                }
//
//                if (bill.dueDate.length<2){
//                    bill.dueDate = "0${bill.dueDate}"
//                }
//
//                val year= cal.get(Calendar.YEAR)
//                val startDate = "1/$month/$year"
//                val endDate = "31/$month/$year"
//                val exisitng = upcomingBillsDao.queryExistingBill(bill.billId, startDate,endDate)
//                if (exisitng.isEmpty()){
//                    val newUpcomingBill = UpcomingBill(
//                        upcomingBillId = UUID.randomUUID().toString(),
//                        billId = bill.billId,
//                        name = bill.name,
//                        amount = bill.amount,
//                        frequency = bill.frequency,
//                        dueDate = DateTimeUtils.createDateFromDay(bill.dueDate),
//                        userId = bill.userId,
//                        paid = false,
//                        synced = false
//                    )
//                    insertUpcomingBill(newUpcomingBill)
//                }
//            }
//        }
//    }
//    suspend fun createRecurringMonthlyBills(){
//        withContext(Dispatchers.IO){
//            val monthlyBills = billDao.getRecurringBills(Constants.MONTHLY)
//            val startDate = DateTimeUtils.getFirstDayOfMonth()
//            val endDate = DateTimeUtils.getLastDayOfMonth()
//            val month = DateTimeUtils.getCurrentMonth()
//            val year = DateTimeUtils.getCurrentYear()
//            monthlyBills.forEach{ bill ->
//                val existing = upcomingBillsDao.queryExistingBill(bill.billId, startDate,endDate)
//                if(existing.isEmpty()){
//                    val newUpcomingBill = UpcomingBill(
//                        upcomingBillId = UUID.randomUUID().toString(),
//                        billId = bill.billId,
//                        name =  bill.name,
//                        amount = bill.amount,
//                        frequency = bill.frequency,
//                        dueDate = "${bill.dueDate}/$month/$year",
//                        userId = bill.userId,
//                        paid = false,
//                        synced = false
//                    )
//                    upcomingBillsDao.insertUpcomingBill(newUpcomingBill)
//
//                }
//            }
//        }
//    }
//
//    suspend fun createRecurringWeeklyBills(){
//        withContext(Dispatchers.IO){
//            val weeklyBills = billDao.getRecurringBills(Constants.WEEKLY)
//            val startDate = DateTimeUtils.getFirstDayOfWeek()
//            val endDate = DateTimeUtils.getLastDayOfWeek()
//            weeklyBills.forEach{ bill ->
//                val existing = upcomingBillsDao.queryExistingBill(bill.billId, startDate,endDate)
//                if (existing.isEmpty()){
//                    val newWeeklyBill = UpcomingBill(
//                        upcomingBillId = UUID.randomUUID().toString(),
//                        billId = bill.billId,
//                        name =  bill.name,
//                        amount = bill.amount,
//                        frequency = bill.frequency,
//                        dueDate = DateTimeUtils.getDateOfWeekDay(bill.dueDate),
//                        userId = bill.userId,
//                        paid = false,
//                        synced = false
//                    )
//                    upcomingBillsDao.insertUpcomingBill(newWeeklyBill)
//                }
//            }
//        }
//    }
//    suspend  fun createRecurringAnnualBills() {
//        withContext(Dispatchers.IO) {
//            val annualBills= billDao.getRecurringBills(Constants.ANNUAL)
//            val currentYear= DateTimeUtils.getCurrentYear()
//            val startDate="01/01/$currentYear"
//            val endDate="31/12/$currentYear"
//
//            annualBills.forEach { bill ->
//                val existing = upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
//                if (existing.isEmpty()) {
//                    val newUpComingAnnualBill = UpcomingBill(
//                        upcomingBillId = UUID.randomUUID().toString(),
//                        billId = bill.billId,
//                        name = bill.name,
//                        amount = bill.amount,
//                        frequency = bill.frequency,
//                        dueDate = DateTimeUtils.getDateOfWeekDay(bill.dueDate),
//                        userId = bill.userId,
//                        paid = false,
//                        synced = false
//                    )
//                    upcomingBillsDao.insertUpcomingBill(newUpComingAnnualBill)
//
//                }
//            }
//        }
//
//    }
//
//
//
//
//    fun getUpcomingBillsByFrequency(freq: String): LiveData<List<UpcomingBill>> {
//        return upcomingBillsDao.getUpcomingBillsByFrequency(freq, paid=false)
//    }
//    fun getPaidBills(){
//
//    }
//
//    fun getAuthToken(): String{
//        val prefs = BillzApp.appContext
//            .getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
//        var token = prefs.getString(Constants.ACCESS_TOKEN, Constants.EMPTY_STRING)
//        token = "Bearer $token"
//        return token
//    }
//
//    suspend fun syncedBills(){
//        withContext(Dispatchers.IO){
//            var token = getAuthToken()
//          val unsyncedBills = billDao.getUnsyncedBills()
//            unsyncedBills.forEach { bill ->
//                val response = apiClient.postBill(token, bill)
//                if (response.isSuccessful){
//                    bill.synced = true
//                    billDao.insertBill(bill)
//                }
//            }
//        }
//    }
//    suspend fun syncUpcomingBills(){
//        withContext(Dispatchers.IO){
//            var token = getAuthToken()
//            upcomingBillsDao.getUnsyncedUpcomingBills().toString().forEach { upcomingBill->
//                val response = apiClient.postUpcomingBill(token, upcomingBill)
//                if (response.isSuccessful){
//                    UpcomingBill.synced = true
//                    upcomingBillsDao.updateUpcomingBill(upcomingBill)
//            }
//            }
//        }
//    }
//    suspend fun fetchRemoteBills(){
//        withContext(Dispatchers.IO){
//            val response = apiClient.fetchRemoteBills(getAuthToken())
//            if(response.isSuccessful){
//                response.body()?.forEach {
//                    Bill.synced = true
//                    bill ->  billDao.insertBill(bill) }
//            }
//        }
//    }
//    suspend fun fetchRemoteUpcomingBills(){
//        withContext(Dispatchers.IO){
//            val response = apiClient.fetchRemoteUpcomingBills(getAuthToken())
//            if(response.isSuccessful){
//                response.body()?.forEach {
//                    UpcomingBill.synced = true
//                        UpcomingBill ->  billDao.insertUpcomingBill(UpcomingBill) }
//            }
//        }
//    }
//
//    suspend fun getMonthlySummary(): MutableLiveData<BillsSummary> {
//        return withContext(Dispatchers.IO){
//            val startDate = DateTimeUtils.getFirstDayOfMonth()
//            val endDate = DateTimeUtils.getLastDayOfMonth()
//            val today = DateTimeUtils.getDateToday()
//            val paid = upcomingBillsDao.getPaidMonthlyBillsSum(startDate, endDate)
//            val upcoming = upcomingBillsDao.getUpcomingBillsThisMonth(startDate, endDate, today)
//            val total = upcomingBillsDao.getTotalMonthlyBills(startDate, endDate)
//            val overdue = upcomingBillsDao.getOverdueBillsThisMonth(startDate, endDate, today)
//            val summary = BillsSummary(paid=paid, upcoming=upcoming, overdue=overdue, total=total)
//            MutableLiveData(summary)
//        }
//    }
//
//}

