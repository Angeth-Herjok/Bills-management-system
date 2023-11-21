package com.example.bills_management_system.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bills_management_system.model.Bill
import com.example.bills_management_system.model.UpcomingBill

@Database(entities = [Bill::class, UpcomingBill::class], version = 5)
abstract class BillzDb: RoomDatabase(){
    abstract fun billDao(): BillDao

    abstract fun upcomingBillsDao(): UpcomingBillsDao
    companion object{
        var database: BillzDb? = null
         fun getDatabase(context: Context): BillzDb{
             if(database==null){
                 database = Room
                     .databaseBuilder(context, BillzDb::class.java,"BillzDb")
                     .fallbackToDestructiveMigration()
                     .build()
             }
             return database as BillzDb
         }
    }
}


