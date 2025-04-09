package com.example.controledegastos.data.repository

import androidx.lifecycle.LiveData
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.data.local.dao.ItemsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemsRepository @Inject constructor(private val itemsDao: ItemsDao) {

    val allItems: LiveData<List<Items>> = itemsDao.getAllItems()

    suspend fun insertItem(item: Items) {
        withContext(Dispatchers.IO) {
            itemsDao.insertItem(item)
        }
    }

    suspend fun updateItem(item: Items) {
        withContext(Dispatchers.IO) {
            itemsDao.updateItem(item)
        }
    }

    suspend fun deleteItem(id: Int) {
        withContext(Dispatchers.IO) {
            itemsDao.deleteItem(id)
        }
    }

    suspend fun deleteItemMonth(monthNumber: String) {
        withContext(Dispatchers.IO) {
            itemsDao.deleteItemMonth(monthNumber)
        }
    }

    fun getMonth(monthNumber: String): LiveData<List<Items>> {
        return itemsDao.getMonth(monthNumber)
    }

    //Filtro por entrada/saída de determinado mês
    fun getMonthFlow(monthNumber: String, flow: String): LiveData<List<Items>> {
        return itemsDao.getMonthFlow(monthNumber, flow)
    }

    fun getIOFiltered(io: String): LiveData<List<Items>> {
        return itemsDao.getIOFiltered(io)
    }

    fun getCategory(category: String): LiveData<List<Items>> {
        return itemsDao.getCategory(category)
    }

    //Filtro por mês e categoria
    fun getMonthCtg(monthNumber: String, category: String? = null): LiveData<List<Items>> {
        return itemsDao.getMonthCtg(monthNumber, category)
    }

}