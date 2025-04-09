package com.example.controledegastos.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ItemsDaoSum{

    @Query("SELECT SUM(value) FROM Items")
    fun getSumValue(): Double

    @Query("SELECT SUM(value) FROM Items where category == :category")
    fun getSumValueCtg(category: String): Double

    @Query("SELECT SUM(value) FROM Items where monthNumber == :monthNumber ")
    fun getSumMonthValue(monthNumber:String): Double

    @Query("SELECT SUM(value) FROM Items where io == :flow")
    fun getSumTotalFlowValue(flow: String): Double

    @Query("SELECT SUM(value) FROM Items where monthNumber == :monthNumber and io == :flow ")
    fun getSumFlowValue(monthNumber:String, flow:String): Double

    //Soma de saídas por categoria de determinado mês
    @Query("SELECT SUM(value) FROM Items where monthNumber == :monthNumber " +
            "and io == :outflow  and category == :category")
    fun getSumOutflowValueCtg(monthNumber:String, outflow:String, category: String? = null): Double

    //Soma total por categoria de determinado mês (saldo total)
    @Query("SELECT SUM(value) FROM Items where monthNumber == :monthNumber and category == :category")
    fun getSumMonthValueCtg(monthNumber:String, category: String? = null): Double

    @Query("SELECT SUM(value) FROM Items")
    fun getSumTotalValueFloat(): Float

    @Query("SELECT SUM(value) FROM Items where monthNumber == :monthNumber ")
    fun getSumMonthValueFloat(monthNumber:String): Float

    @Query("SELECT SUM(value) FROM Items where monthNumber == :monthNumber and io == :outflow ")
    fun getSumOutflowValueFloat(monthNumber:String, outflow:String): Float

    @Query("SELECT SUM(value) FROM Items where io == :outflow")
    fun getSumOutflowTotalValueFloat(outflow:String): Float

}