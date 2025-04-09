package com.example.controledegastos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.controledegastos.data.local.dao.ItemsDao
import com.example.controledegastos.data.local.dao.ItemsDaoSum
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.data.repository.ItemsRepository
import com.example.controledegastos.data.repository.ItemsSumRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class RepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockDao = mock(ItemsDao::class.java)
    private val mockDaoSum = mock(ItemsDaoSum::class.java)

    private val itemsRepository = ItemsRepository(mockDao)
    private val itemsSumRepository = ItemsSumRepository(mockDaoSum)

    @Test
    fun `testando insert item no repository`(): Unit = runBlocking {
        val item = Items(
            1, "descrição", "observação", "Entrada",
            "Pagamento á vista", 100.0,
            "10/02/2025",
            "1", "Transporte"
        )

        itemsRepository.insertItem(item)
        verify(mockDao).insertItem(item)

    }

    @Test
    fun `testando update item no repository`(): Unit = runBlocking {
        val item = Items(
            1, "descrição 1", "observação 1", "Entrada",
            "Pagamento á vista", 500.0,
            "10/02/2025",
            "1", "Transporte"
        )

        val item2 = Items(
            1, "descrição 2", "observação 2", "Entrada",
            "Pagamento á vista", 500.0,
            "10/02/2025",
            "1", "Saúde"
        )
        itemsRepository.insertItem(item)
        itemsRepository.updateItem(item2)

        verify(mockDao).updateItem(item2)

    }

    @Test
    fun `testando delete item no repository`(): Unit = runBlocking {
        val item = Items(
            1, "descrição", "observação", "Entrada",
            "Pagamento á vista", 100.0,
            "10/02/2025",
            "1", "Transporte"
        )

        itemsRepository.insertItem(item)
        itemsRepository.deleteItem(1)

        verify(mockDao).deleteItem(1)
    }

    @Test
    fun `testando getSumValue no repository`(): Unit = runBlocking {
        itemsSumRepository.getSumValue()
        verify(mockDaoSum).getSumValue()
    }
    @Test
    fun `testando getSumValueCtg no repository`(): Unit = runBlocking {
        itemsSumRepository.getSumValueCtg("Transporte")
        verify(mockDaoSum).getSumValueCtg("Transporte")
    }
    @Test
    fun `testando getSumMonthValue no repository`() {
        itemsSumRepository.getSumMonthValue("02")
        verify(mockDaoSum).getSumMonthValue("02")
    }
    @Test
    fun `testando getSumTotalFlowValue no repository`(): Unit = runBlocking {
        itemsSumRepository.getSumTotalFlowValue("Entrada")
        verify(mockDaoSum).getSumTotalFlowValue("Entrada")
    }
    @Test
    fun `testando getSumFlowValue no repository`() {
        itemsSumRepository.getSumFlowValue("02", "Entrada")
        verify(mockDaoSum).getSumFlowValue("02", "Entrada")
    }
    @Test
    fun `testando getSumOutflowValueCtg no repository`(): Unit = runBlocking {
        itemsSumRepository.getSumOutflowValueCtg("02", "Saída", "Transporte")
        verify(mockDaoSum).getSumOutflowValueCtg("02", "Saída", "Transporte")
    }
    @Test
    fun `testando getSumMonthValueCtg no repository`(): Unit = runBlocking {
        itemsSumRepository.getSumMonthValueCtg("02", "Transporte")
        verify(mockDaoSum).getSumMonthValueCtg("02", "Transporte")
    }
    @Test
    fun `testando getSumTotalValueFloat no repository`(): Unit = runBlocking {
        itemsSumRepository.getSumTotalValueFloat()
        verify(mockDaoSum).getSumTotalValueFloat()
    }
    @Test
    fun `testando getSumMonthValueFloat no repository`() {
        itemsSumRepository.getSumMonthValueFloat("02")
        verify(mockDaoSum).getSumMonthValueFloat("02")
    }
    @Test
    fun `testando getSumOutflowValueFloat no repository`() {
        itemsSumRepository.getSumOutflowValueFloat("02", "Saída")
        verify(mockDaoSum).getSumOutflowValueFloat("02", "Saída")
    }
    @Test
    fun `testando getSumOutflowTotalValueFloat no repository`(): Unit = runBlocking {
        itemsSumRepository.getSumOutflowTotalValueFloat("Saída")
        verify(mockDaoSum).getSumOutflowTotalValueFloat("Saída")
    }
    @Test
    fun `testando getAllItems no repository`() {
        itemsRepository.allItems
        verify(mockDao).getAllItems()
    }
    @Test
    fun `testando getMonth no repository`() {
        itemsRepository.getMonth("02")
        verify(mockDao).getMonth("02")
    }
    @Test
    fun `testando getCategory no repository`() {
        itemsRepository.getCategory("Transporte")
        verify(mockDao).getCategory("Transporte")
    }
    @Test
    fun `testando getMonthCtg no repository`() {
        itemsRepository.getMonthCtg("02", "Transporte")
        verify(mockDao).getMonthCtg("02", "Transporte")
    }
    @Test
    fun `testando getIOFiltered no repository`() {
        itemsRepository.getIOFiltered("Entrada")
        verify(mockDao).getIOFiltered("Entrada")
    }
    @Test
    fun `testando getMonthFlow no repository`() {
        itemsRepository.getMonthFlow("02", "Entrada")
        verify(mockDao).getMonthFlow("02", "Entrada")
    }


}