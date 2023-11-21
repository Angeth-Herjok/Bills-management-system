package com.example.bills_management_system.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bills_management_system.model.Bill
import com.example.bills_management_system.model.BillsSummary
import com.example.bills_management_system.model.UpcomingBill
import com.example.bills_management_system.repository.BillRepository
import kotlinx.coroutines.launch

class BillsViewModel: ViewModel() {
    var billRepo= BillRepository()
    val summaryLiveData = MediatorLiveData<BillsSummary>()

    fun saveBill(bill: Bill){
        viewModelScope.launch {
            billRepo.saveBill(bill)
        }

    }
//    fun getAllBills(): LiveData<List<Bill>> {
//        return billRepo.getAllBills()
//    }
        fun createRecurringBills(){
        viewModelScope.launch {
            billRepo.createRecurringMonthlyBills()
            billRepo.createRecurringWeeklyBills()
            billRepo.createRecurringAnnualBills()
        }
    }

    fun getUpcomingBillsByFreq(freq:String): LiveData<List<UpcomingBill>>{
        return billRepo.getUpcomingBillsByFrequency(freq)
    }
    fun updateUpcomingBill(upcomingBill: UpcomingBill){
        viewModelScope.launch {
            billRepo.updateUpcomingBill(upcomingBill)
        }
    }

    fun getPaidBills(): LiveData<List<UpcomingBill>>{
        return billRepo.getPaidBills()
    }

    fun fetchRemoteBills(){
        viewModelScope.launch {
            billRepo.fetchRemoteBills()
            billRepo.fetchRemoteUpcomingBills()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonthlySummary(){
        viewModelScope.launch {
            summaryLiveData.postValue(billRepo.getMonthlySummary().value)
        }
    }

}