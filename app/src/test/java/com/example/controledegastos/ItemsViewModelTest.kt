import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.data.repository.ItemsRepository
import com.example.controledegastos.data.repository.ItemsSumRepository
import com.example.controledegastos.viewmodel.ItemsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class ItemsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var itemsRepository: ItemsRepository
    private lateinit var itemsSumRepository: ItemsSumRepository
    private lateinit var viewModel: ItemsViewModel

    @Before
    fun setup() {
        itemsRepository = mock()
        itemsSumRepository = mock()
        viewModel = ItemsViewModel(itemsRepository, itemsSumRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verifica se o método insertItem do livedata insere o item corretamente`() = runTest {

        val item = Items(
            1, "descrição", "observação", "Entrada",
            "Pagamento á vista", 100.0,
            "10/02/2025",
            "1", "Transporte"
        )

        viewModel.insertItem(item)
        testDispatcher.scheduler.advanceUntilIdle() //aguarda a execução das coroutines
        verify(itemsRepository).insertItem(item)

    }

    @Test
    fun `verifica se o método updateItem do livedata atualiza o item corretamente`() = runTest {

        val item = Items(
            1, "descrição", "observação", "Entrada",
            "Pagamento á vista", 100.0,
            "10/02/2025",
            "1", "Transporte"
        )

        val item2 = Items(
            2, "descrição 2", "observação 2", "Entrada",
            "Pagamento á vista", 200.0,
            "10/02/2025",
            "1", "Transporte"
        )

        viewModel.insertItem(item)
        viewModel.updateItem(item2)
        testDispatcher.scheduler.advanceUntilIdle() //aguarda a execução das coroutines
        verify(itemsRepository).updateItem(item2)

    }

    @Test
    fun `verifica se o método deleteItem deleta o item corretamente`() = runTest {
        val item = Items(
            1, "descrição", "observação", "Entrada",
            "Pagamento á vista", 100.0,
            "10/02/2025",
            "1", "Transporte"
        )
        viewModel.insertItem(item)
        viewModel.deleteItem(1)

        verify(itemsRepository).deleteItem(1)

    }

    @Test
    fun `verifica se o método deleteItemMonth deleta o item corretamente`() = runTest {
        val item = Items(
            1, "descrição", "observação", "Entrada",
            "Pagamento á vista", 100.0,
            "10/02/2025",
            "1", "Transporte"
        )
        viewModel.insertItem(item)
        viewModel.deleteItemMonth("1")

        verify(itemsRepository).deleteItemMonth("1")

    }

    @Test
    fun `verifica se getMonth retorna um LiveData do repository`() {
        val monthNumber = "02"
        val liveData = MutableLiveData<List<Items>>()

        `when`(itemsRepository.getMonth(monthNumber)).thenReturn(liveData)
        val result = viewModel.getMonth(monthNumber)

        assertEquals(liveData, result)
    }

    @Test
    fun `verifica se getMonthFlow retorna um LiveData do repository`() {

        val monthNumber = "02"
        val flow = "Entrada"
        val liveData = MutableLiveData<List<Items>>()

        `when`(itemsRepository.getMonthFlow(monthNumber, flow)).thenReturn(liveData)
        val result = viewModel.getMonthFlow(monthNumber, flow)
        assertEquals(liveData, result)
    }
    @Test
    fun `verifica se getIOFiltered retorna um LiveData do repository`() {
        val io = "Entrada"
        val liveData = MutableLiveData<List<Items>>()

        `when`(itemsRepository.getIOFiltered(io)).thenReturn(liveData)
        val result = viewModel.getIOFiltered(io)
        assertEquals(liveData, result)

    }
    @Test
    fun `verifica se getCategory retorna um LiveData do repository`() {
        val category = "Transporte"
        val liveData = MutableLiveData<List<Items>>()

        `when`(itemsRepository.getCategory(category)).thenReturn(liveData)
        val result = viewModel.getCategory(category)
        assertEquals(liveData, result)
    }
    @Test
    fun `verifica se getMonthCtg retorna um LiveData do repository`() {
        val monthNumber = "02"
        val category = "Transporte"
        val liveData = MutableLiveData<List<Items>>()

        `when`(itemsRepository.getMonthCtg(monthNumber, category)).thenReturn(liveData)
        val result = viewModel.getMonthCtg(monthNumber, category)
        assertEquals(liveData, result)
    }


}