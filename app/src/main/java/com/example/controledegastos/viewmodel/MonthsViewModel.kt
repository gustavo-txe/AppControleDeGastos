package com.example.controledegastos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controledegastos.data.usecase.MonthTotalValue
import com.example.controledegastos.ui.model.MonthSummaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class MonthsViewModel @Inject constructor(
    private val monthTotalValue: MonthTotalValue
) : ViewModel() {

    private val mMonths = MutableLiveData<List<MonthSummaryUi>>()
    val months: LiveData<List<MonthSummaryUi>> get() = mMonths

    fun loadMonths(monthNames: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val values = monthNames.mapIndexed { index, month ->
                val monthNumber = index.toString()
                val inflow = monthTotalValue.inflow(monthNumber)
                val outflow = monthTotalValue.totalOutflow(monthNumber)
                val balance = monthTotalValue.totalBalance(monthNumber)
                val totalBalancePie = monthTotalValue.totalBalancePie(monthNumber)
                val outflowTotalPie = monthTotalValue.outflowTotalPie(monthNumber)

                var inflowPie = totalBalancePie - outflowTotalPie
                if (outflowTotalPie == 0f && inflowPie == 0f) {
                    inflowPie = 1f
                }

                MonthSummaryUi(
                    monthIndex = index,
                    monthName = month,
                    inflowText = NumberFormat.getCurrencyInstance().format(inflow),
                    outflowText = NumberFormat.getCurrencyInstance().format(outflow),
                    balanceText = NumberFormat.getCurrencyInstance().format(balance),
                    inflowPie = inflowPie,
                    outflowPie = -outflowTotalPie,
                    hasData = !(balance == 0.0 && outflow == 0.0)
                )
            }
            mMonths.postValue(values)
        }
    }
}
