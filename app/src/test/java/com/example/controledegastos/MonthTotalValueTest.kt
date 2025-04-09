package com.example.controledegastos

import android.content.Context
import com.example.controledegastos.data.repository.ItemsSumRepository
import com.example.controledegastos.data.usecase.MonthTotalValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class MonthTotalValueTest {
    private val itemsSumRepository: ItemsSumRepository = mock()
    private lateinit var monthTotalValue: MonthTotalValue

    @Before
    fun setUp() {
        val context = mock<Context>()
        monthTotalValue = MonthTotalValue(context, itemsSumRepository)
    }

    @Test
    fun `verifica se totalBalancePie retorna o valor correto de Saldo do mês`() = runTest {
        val month = "02"
        val expectedValue = 500f

        whenever(itemsSumRepository.getSumMonthValueFloat(month)).thenReturn(expectedValue)
        val result = monthTotalValue.totalBalancePie(month)

        assertEquals(expectedValue, result)
    }

    @Test
    fun `verifica se outflowTotalPie retorna o valor correto de Saída do mês`() = runTest {
        val month = "05"
        val expectedValue = 200f

        whenever(itemsSumRepository.getSumOutflowValueFloat(month, "Saída")).thenReturn(expectedValue)
        val result = monthTotalValue.outflowTotalPie(month)

        assertEquals(expectedValue, result)
    }

    @Test
    fun `verifica se totalBalance retorna o valor total correto de Saldo do mês`() = runTest {
        val month = "03"
        val expectedValue = 1000.0

        whenever(itemsSumRepository.getSumMonthValue(month)).thenReturn(expectedValue)
        val result = monthTotalValue.totalBalance(month)

        assertEquals(expectedValue, result, 0.0)
    }

    @Test
    fun `verifica se totalOutflow retorna o valor total correto de Saída do mês`() = runTest {
        val month = "08"
        val expectedValue = 300.0

        whenever(itemsSumRepository.getSumFlowValue(month, "Saída")).thenReturn(expectedValue)
        val result = monthTotalValue.totalOutflow(month)

        assertEquals(expectedValue, result, 0.0)
    }

    @Test
    fun `verifica se inflow retorna o valor correto de Entrada do mês`() = runTest {
        val month = "05"
        val balance = 1000.0
        val outflow = 300.0

        whenever(itemsSumRepository.getSumMonthValue(month)).thenReturn(balance)
        whenever(itemsSumRepository.getSumFlowValue(month, "Saída")).thenReturn(outflow)

        val result = monthTotalValue.inflow(month)

        assertEquals(balance - outflow, result, 0.0)
    }
}