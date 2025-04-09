package com.example.controledegastos.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.data.repository.ItemsRepository
import com.example.controledegastos.data.repository.ItemsSumRepository
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    private val itemsSumRepository: ItemsSumRepository) : ViewModel() {

    private val allItems: LiveData<List<Items>> = itemsRepository.allItems

    private val mNumberFormatBalance: MutableLiveData<String> = MutableLiveData()
    val numberBalance: LiveData<String> get() = mNumberFormatBalance

    //Valor total anual para exibição gráfica em barras
    private val mBardata: MutableLiveData<BarData> = MutableLiveData()
    val barData: LiveData<BarData> get() = mBardata

    private val mBalanceBardata: MutableLiveData<String> = MutableLiveData()
    val balanceBardata: LiveData<String> get() = mBalanceBardata

    private val mInflowBardata: MutableLiveData<String> = MutableLiveData()
    val inflowBardata: LiveData<String> get() = mInflowBardata

    private val mOutflowBardata: MutableLiveData<String> = MutableLiveData()
    val outflowBardata: LiveData<String> get() = mOutflowBardata


    //Cálculo de valores mensais
    private val mInflowMonth: MutableLiveData<String> = MutableLiveData()
    val inflowMonth: LiveData<String> get() = mInflowMonth

    private val mOutflowMonth: MutableLiveData<String> = MutableLiveData()
    val outflowMonth: LiveData<String> get() = mOutflowMonth

    private val mBalanceMonth: MutableLiveData<String> = MutableLiveData()
    val balanceMonth: LiveData<String> get() = mBalanceMonth


    //Cálculo de valores mensais filtrados por Entrada/Saída
    private val mInflowTotal: MutableLiveData<String> = MutableLiveData()
    val inflowTotal: LiveData<String> get() = mInflowTotal

    private val mOutflowTotal: MutableLiveData<String> = MutableLiveData()
    val outflowTotal: LiveData<String> get() = mOutflowTotal

    private val mBalanceTotal: MutableLiveData<String> = MutableLiveData()
    val balanceTotal: LiveData<String> get() = mBalanceTotal

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

    fun getAllItems(): LiveData<List<Items>> {
        return allItems
    }

    fun getMonth(monthNumber: String): LiveData<List<Items>> {
        return itemsRepository.getMonth(monthNumber)
    }

    fun getMonthFlow(monthNumber: String, flow: String): LiveData<List<Items>> {
        return itemsRepository.getMonthFlow(monthNumber, flow)
    }

    fun getIOFiltered(io: String): LiveData<List<Items>> {
        return itemsRepository.getIOFiltered(io)
    }

    fun getCategory(category: String): LiveData<List<Items>> {
        return itemsRepository.getCategory(category)
    }

    fun getMonthCtg(monthNumber: String, category: String? = null): LiveData<List<Items>> {
        return itemsRepository.getMonthCtg(monthNumber, category)
    }

    fun updateValueCtg(ctg: String, list: ArrayList<Items>) {

        for (i in 0..list.size) {

            viewModelScope.launch(Dispatchers.IO) {
                val totalBalanceT = itemsSumRepository.getSumValueCtg(ctg)
                val nfTBalance = NumberFormat.getCurrencyInstance().format(totalBalanceT)
                mNumberFormatBalance.postValue(nfTBalance)
            }
        }
    }

    fun updateTotalValue(list: ArrayList<Items>) {

        for (i in 0..list.size) {

            viewModelScope.launch(Dispatchers.IO) {
                val totalBalanceT = itemsSumRepository.getSumValue()
                val nfTBalance = NumberFormat.getCurrencyInstance().format(totalBalanceT)
                mNumberFormatBalance.postValue(nfTBalance)
            }
        }
    }

    fun updateValueFlow(flow: String, list: ArrayList<Items>) {

        for (i in 0..list.size) {

            viewModelScope.launch(Dispatchers.IO) {
                val totalBalanceT = itemsSumRepository.getSumTotalFlowValue(flow)
                val nfTBalance = NumberFormat.getCurrencyInstance().format(totalBalanceT)
                mNumberFormatBalance.postValue(nfTBalance)
            }
        }
    }

    //Carregar valor total anual para exibição gráfica
    fun barData() {

        viewModelScope.launch(Dispatchers.IO) {

            val entries: ArrayList<BarEntry> = ArrayList()

            val totalBalance = itemsSumRepository.getSumTotalValueFloat()
            val totalOutflow = itemsSumRepository.getSumOutflowTotalValueFloat("Saída")
            val inflow = totalBalance - totalOutflow

            val totalBalanceT = itemsSumRepository.getSumValue()
            val outflowTotalT = itemsSumRepository.getSumTotalFlowValue("Saída")
            val inflowT = totalBalanceT - outflowTotalT

            val nfT = NumberFormat.getCurrencyInstance().format(inflowT)
            val nfTOutflow = NumberFormat.getCurrencyInstance().format(outflowTotalT)
            val nfTBalance = NumberFormat.getCurrencyInstance().format(totalBalanceT)

            val outflowPositive = -totalOutflow
            val totalBalancePositive: Float = inflow - outflowPositive

            mInflowBardata.postValue(nfT)
            mOutflowBardata.postValue(nfTOutflow)
            mBalanceBardata.postValue(nfTBalance)

            entries.add(BarEntry(100f, inflow, "Entrada Total"))
            entries.add(BarEntry(101.5f, -totalOutflow, "Saída Total"))
            entries.add(BarEntry(103f, totalBalancePositive, "SaldoTotal"))

            val GREEN = Color.parseColor("#4CAF50")
            val RED = Color.parseColor("#DF4646")
            val BLUE = Color.parseColor("#3042A8")

            val barDataSet = BarDataSet(entries, "Ganhos / Despesas Total (Em R$)").apply {
                setColors(GREEN, RED, BLUE)
                valueTextSize = 1f
                valueTextColor = Color.BLACK
            }

            val barData = BarData(barDataSet)
            mBardata.postValue(barData)

        }
    }

    //Carregar valores totais filtrados por mês e categoria
    fun loadAllitemsFiltered(month: String, ctg: String? = null) {

        viewModelScope.launch(Dispatchers.IO) {

            var totalBalance = itemsSumRepository.getSumMonthValue(month)
            var totalOutflow = itemsSumRepository.getSumFlowValue(month, "Saída")

            if (ctg != null) {
                totalBalance = itemsSumRepository.getSumMonthValueCtg(month, ctg)
                totalOutflow = itemsSumRepository.getSumOutflowValueCtg(month, "Saída", ctg)
            }

            val inflow = totalBalance - totalOutflow
            val nf = NumberFormat.getCurrencyInstance().format(inflow)
            val nfOutflow = NumberFormat.getCurrencyInstance().format(totalOutflow)
            val nfBalance = NumberFormat.getCurrencyInstance().format(totalBalance)

            mInflowMonth.postValue(nf)
            mOutflowMonth.postValue(nfOutflow)
            mBalanceMonth.postValue(nfBalance)

        }
    }

    //Carregar valores totais filtrados por mês
    fun loadAllitemsFlow(flow: String, month: String) {

        viewModelScope.launch(Dispatchers.IO) {

            val totalBalance = itemsSumRepository.getSumMonthValue(month)
            val totalOutflow = itemsSumRepository.getSumFlowValue(month, flow)
            val totalInflow = itemsSumRepository.getSumFlowValue(month, "Saída")

            val inflow = totalBalance - totalInflow

            val nfInflow = NumberFormat.getCurrencyInstance().format(inflow)
            val nfOutflow = NumberFormat.getCurrencyInstance().format(totalOutflow)
            val nfNullValue = NumberFormat.getCurrencyInstance().format(0.00)

            if(flow == "Entrada"){
                mInflowTotal.postValue(nfInflow)
                mOutflowTotal.postValue(nfNullValue)
                mBalanceTotal.postValue(nfNullValue)
            }else{
                mInflowTotal.postValue(nfNullValue)
                mOutflowTotal.postValue(nfOutflow)
                mBalanceTotal.postValue(nfNullValue)
            }
        }
    }

}