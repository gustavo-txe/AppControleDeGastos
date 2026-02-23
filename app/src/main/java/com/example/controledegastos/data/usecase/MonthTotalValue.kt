package com.example.controledegastos.data.usecase

import com.example.controledegastos.data.repository.ItemsSumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonthTotalValue @Inject constructor(
    private val itemsSumRepository: ItemsSumRepository
) {

    suspend fun totalBalancePie(month: String): Float = withContext(Dispatchers.IO) {
        return@withContext itemsSumRepository.getSumMonthValueFloat(month)
    }

    suspend fun outflowTotalPie(month: String): Float = withContext(Dispatchers.IO) {
        return@withContext itemsSumRepository.getSumOutflowValueFloat(month, "Saída")
    }

    suspend fun totalBalance(month: String): Double = withContext(Dispatchers.IO) {
        return@withContext itemsSumRepository.getSumMonthValue(month)
    }

    suspend fun totalOutflow(month: String): Double = withContext(Dispatchers.IO) {
        return@withContext itemsSumRepository.getSumFlowValue(month, "Saída")
    }

    suspend fun inflow(month: String): Double = withContext(Dispatchers.IO) {
        return@withContext totalBalance(month) - totalOutflow(month)
    }

}
