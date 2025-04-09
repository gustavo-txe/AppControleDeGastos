package com.example.controledegastos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.controledegastos.data.model.Items

@Dao
interface ItemsDao{

    @Insert
    fun insertItem(items: Items)

    @Insert
    fun insertAllItems(items: List<Items>)

    @Update
    fun updateItem(items: Items)

    @Query("Delete from Items where id = :id")
    fun deleteItem(id:Int)

    @Query("Delete from Items where monthNumber = :monthNumber")
    fun deleteItemMonth(monthNumber: String)

    @Query("SELECT * from Items")
    fun getAllItems(): LiveData<List<Items>>

    @Query("SELECT * from Items where monthNumber == :monthNumber")
    fun getMonth(monthNumber: String): LiveData<List<Items>>

    //Filtro por entrada/saída de determinado mês
    @Query("SELECT * from Items where monthNumber == :monthNumber and io == :flow")
    fun getMonthFlow(monthNumber: String, flow: String): LiveData<List<Items>>

    @Query("SELECT monthNumber FROM Items")
    fun getDateMonth(): String

    @Query("SELECT * from Items where io == :io")
    fun getIOFiltered(io: String): LiveData<List<Items>>

    @Query("SELECT * from Items where category == :category")
    fun getCategory(category: String): LiveData<List<Items>>

    //Filtro por mês e categoria
    @Query("SELECT * from Items where monthNumber == :monthNumber and category == :category")
    fun getMonthCtg(monthNumber: String, category: String? = null): LiveData<List<Items>>

}
