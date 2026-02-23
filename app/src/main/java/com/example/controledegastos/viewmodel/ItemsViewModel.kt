package com.example.controledegastos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controledegastos.data.model.FlowType
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.data.repository.ItemsRepository
import com.example.controledegastos.data.repository.ItemsSumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    private val itemsSumRepository: ItemsSumRepository
) : ViewModel() {

    data class TotalsUiState(
        val inflow: String = NumberFormat.getCurrencyInstance().format(0.0),
        val outflow: String = NumberFormat.getCurrencyInstance().format(0.0),
        val balance: String = NumberFormat.getCurrencyInstance().format(0.0)
    )

    data class AnnualChartUiState(
        val inflow: Double,
        val outflow: Double,
        val balance: Double,
        val inflowText: String,
        val outflowText: String,
        val balanceText: String
    )

    private sealed class MainFilter {
        data object All : MainFilter()
        data class Flow(val flow: String) : MainFilter()
        data class Category(val category: String) : MainFilter()
    }

    private data class MonthFilter(
        val month: String,
        val category: String? = null,
        val flow: String? = null
    )

    private val mMainFilter = MutableLiveData<MainFilter>(MainFilter.All)
    private val mMonthFilter = MutableLiveData<MonthFilter>()

    val mainItems: LiveData<List<Items>> = mMainFilter.switchMap { filter ->
        when (filter) {
            MainFilter.All -> itemsRepository.allItems
            is MainFilter.Flow -> itemsRepository.getIOFiltered(filter.flow)
            is MainFilter.Category -> itemsRepository.getCategory(filter.category)
        }
    }

    val monthItems: LiveData<List<Items>> = mMonthFilter.switchMap { filter ->
        when {
            filter.flow != null -> itemsRepository.getMonthFlow(filter.month, filter.flow)
            filter.category != null -> itemsRepository.getMonthCtg(filter.month, filter.category)
            else -> itemsRepository.getMonth(filter.month)
        }
    }

    private val mNumberFormatBalance = MutableLiveData<String>()
    val numberBalance: LiveData<String> get() = mNumberFormatBalance

    private val mMonthTotals = MutableLiveData<TotalsUiState>()
    val monthTotals: LiveData<TotalsUiState> get() = mMonthTotals

    private val mAnnualChartState = MutableLiveData<AnnualChartUiState>()
    val annualChartState: LiveData<AnnualChartUiState> get() = mAnnualChartState

    fun insertItem(items: Items) = viewModelScope.launch(Dispatchers.IO) {
        itemsRepository.insertItem(items)
    }

    fun updateItem(items: Items) = viewModelScope.launch(Dispatchers.IO) {
        itemsRepository.updateItem(items)
    }

    fun deleteItem(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        itemsRepository.deleteItem(id)
    }

    fun deleteItemMonth(monthNumber: String) = viewModelScope.launch(Dispatchers.IO) {
        itemsRepository.deleteItemMonth(monthNumber)
    }

    fun applyMainAllFilter() {
        mMainFilter.value = MainFilter.All
        refreshMainBalance()
    }

    fun applyMainFlowFilter(flow: String) {
        mMainFilter.value = MainFilter.Flow(flow)
        refreshMainBalance()
    }

    fun applyMainCategoryFilter(category: String) {
        mMainFilter.value = MainFilter.Category(category)
        refreshMainBalance()
    }

    fun applyMonthFilter(month: String, category: String? = null, flow: String? = null) {
        mMonthFilter.value = MonthFilter(month, category, flow)
        refreshMonthTotals(month, category, flow)
    }

    fun refreshMainBalance() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = when (val filter = mMainFilter.value ?: MainFilter.All) {
                MainFilter.All -> itemsSumRepository.getSumValue()
                is MainFilter.Flow -> itemsSumRepository.getSumTotalFlowValue(filter.flow)
                is MainFilter.Category -> itemsSumRepository.getSumValueCtg(filter.category)
            }
            mNumberFormatBalance.postValue(NumberFormat.getCurrencyInstance().format(value))
        }
    }

    fun loadAnnualChartData() {
        viewModelScope.launch(Dispatchers.IO) {
            val totalBalance = itemsSumRepository.getSumValue()
            val totalOutflowAbs = kotlin.math.abs(itemsSumRepository.getSumTotalFlowValue(FlowType.OUTFLOW.value))
            val inflow = totalBalance - (-totalOutflowAbs)

            mAnnualChartState.postValue(
                AnnualChartUiState(
                    inflow = inflow,
                    outflow = -totalOutflowAbs,
                    balance = totalBalance,
                    inflowText = NumberFormat.getCurrencyInstance().format(inflow),
                    outflowText = NumberFormat.getCurrencyInstance().format(-totalOutflowAbs),
                    balanceText = NumberFormat.getCurrencyInstance().format(totalBalance)
                )
            )
        }
    }

    fun refreshMonthTotals(month: String, category: String? = null, flow: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val totals = when {
                flow != null -> {
                    val totalOutflow = itemsSumRepository.getSumFlowValue(month, flow)
                    if (flow == FlowType.INFLOW.value) {
                        TotalsUiState(
                            inflow = NumberFormat.getCurrencyInstance().format(totalOutflow),
                            outflow = NumberFormat.getCurrencyInstance().format(0.0),
                            balance = NumberFormat.getCurrencyInstance().format(0.0)
                        )
                    } else {
                        TotalsUiState(
                            inflow = NumberFormat.getCurrencyInstance().format(0.0),
                            outflow = NumberFormat.getCurrencyInstance().format(totalOutflow),
                            balance = NumberFormat.getCurrencyInstance().format(0.0)
                        )
                    }
                }

                category != null -> {
                    val totalBalance = itemsSumRepository.getSumMonthValueCtg(month, category)
                    val totalOutflow = itemsSumRepository.getSumOutflowValueCtg(month, FlowType.OUTFLOW.value, category)
                    val inflow = totalBalance - totalOutflow
                    TotalsUiState(
                        inflow = NumberFormat.getCurrencyInstance().format(inflow),
                        outflow = NumberFormat.getCurrencyInstance().format(totalOutflow),
                        balance = NumberFormat.getCurrencyInstance().format(totalBalance)
                    )
                }

                else -> {
                    val totalBalance = itemsSumRepository.getSumMonthValue(month)
                    val totalOutflow = itemsSumRepository.getSumFlowValue(month, FlowType.OUTFLOW.value)
                    val inflow = totalBalance - totalOutflow
                    TotalsUiState(
                        inflow = NumberFormat.getCurrencyInstance().format(inflow),
                        outflow = NumberFormat.getCurrencyInstance().format(totalOutflow),
                        balance = NumberFormat.getCurrencyInstance().format(totalBalance)
                    )
                }
            }

            mMonthTotals.postValue(totals)
        }
    }
}
