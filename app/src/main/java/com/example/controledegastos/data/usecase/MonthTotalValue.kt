package com.example.controledegastos.data.usecase

import android.content.Context
import com.example.controledegastos.data.repository.ItemsSumRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonthTotalValue @Inject constructor(
    @ApplicationContext context: Context,
    private val itemsSumRepository: ItemsSumRepository) {

    // Função para obter o saldo total para representação gráfica
    suspend fun totalBalancePie(month: String): Float = withContext(Dispatchers.IO) {
        return@withContext itemsSumRepository.getSumMonthValueFloat(month)
    }

    // Função para obter o valor total de Saída para representação gráfica
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