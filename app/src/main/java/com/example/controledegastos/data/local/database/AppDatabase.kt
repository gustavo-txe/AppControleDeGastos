package com.example.controledegastos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.controledegastos.data.local.dao.ItemsDao
import com.example.controledegastos.data.local.dao.ItemsDaoSum
import com.example.controledegastos.data.model.Items

@Database(entities = [Items::class], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getItemsDao() : ItemsDao
    abstract fun getItemsDaoSum(): ItemsDaoSum

}