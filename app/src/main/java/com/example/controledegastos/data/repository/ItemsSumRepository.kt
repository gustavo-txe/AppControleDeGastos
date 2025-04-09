package com.example.controledegastos.data.repository

import com.example.controledegastos.data.local.dao.ItemsDaoSum
import javax.inject.Inject

class ItemsSumRepository @Inject constructor(private val itemsDaoSum: ItemsDaoSum) {

    suspend fun getSumValue(): Double {
        return itemsDaoSum.getSumValue()
    }

    suspend fun getSumValueCtg(category: String): Double {
        return itemsDaoSum.getSumValueCtg(category)
    }

     fun getSumMonthValue(monthNumber:String): Double {
        return itemsDaoSum.getSumMonthValue(monthNumber)
    }

    suspend fun getSumTotalFlowValue(flow: String): Double {
        return itemsDaoSum.getSumTotalFlowValue(flow)
    }

    fun getSumFlowValue(monthNumber:String, flow:String): Double {
        return itemsDaoSum.getSumFlowValue(monthNumber, flow)
    }

    //Soma de saídas por categoria de determinado mês
    suspend fun getSumOutflowValueCtg(monthNumber:String, outflow:String, category: String? = null): Double {
        return itemsDaoSum.getSumOutflowValueCtg(monthNumber, outflow, category)
    }

    //Soma total por categoria de determinado mês (saldo total)
    suspend fun getSumMonthValueCtg(monthNumber:String, category: String? = null): Double {
        return itemsDaoSum.getSumMonthValueCtg(monthNumber, category)
    }

    suspend fun getSumTotalValueFloat(): Float {
        return itemsDaoSum.getSumTotalValueFloat()
    }

     fun getSumMonthValueFloat(monthNumber:String): Float {
        return itemsDaoSum.getSumMonthValueFloat(monthNumber)
    }

     fun getSumOutflowValueFloat(monthNumber:String, outflow:String): Float {
        return itemsDaoSum.getSumOutflowValueFloat(monthNumber, outflow)
    }

    suspend fun getSumOutflowTotalValueFloat(outflow:String): Float {
        return itemsDaoSum.getSumOutflowTotalValueFloat(outflow)
    }

}