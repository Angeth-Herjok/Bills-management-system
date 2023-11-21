package com.example.bills_management_system.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bills_management_system.BillzApp
import com.example.bills_management_system.model.Bill


@Dao
interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBill(bill: Bill)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBill(bill: BillzApp)

    @Query("SELECT * FROM Bills")
    fun getAllBills(): LiveData<List<BillzApp>>

    @Query("SELECT * FROM Bills WHERE frequency = :freq")
    fun getRecurringBills(freq: String): List<BillzApp>

    @Query ("SELECT * FROM Bills WHERE synced=0")
    fun getUnsyncedBills(): List<Bill>
}



