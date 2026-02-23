package com.example.controledegastos.ui.features.bardata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import androidx.activity.viewModels
import com.example.controledegastos.databinding.ActivityGratBinding
import com.example.controledegastos.viewmodel.ItemsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGratBinding

    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGratBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIconGratActivity.setOnClickListener { onBackPressed() }

        itemsViewModel.loadAnnualChartData()

        binding.barGrat.setOnLongClickListener {
            itemsViewModel.loadAnnualChartData()
            true
        }

        setupObservers()
    }

    private fun setupObservers() {

        val barChart: BarChart = binding.barGrat

        itemsViewModel.annualChartState.observe(this@BarDataActivity) { state ->
            val entries = arrayListOf(
                BarEntry(100f, state.inflow.toFloat(), "Entrada Total"),
                BarEntry(101.5f, state.outflow.toFloat(), "Saída Total"),
                BarEntry(103f, state.balance.toFloat(), "SaldoTotal")
            )

            val barDataSet = BarDataSet(entries, "Ganhos / Despesas Total (Em R$)").apply {
                setColors(
                    Color.parseColor("#4CAF50"),
                    Color.parseColor("#DF4646"),
                    Color.parseColor("#3042A8")
                )
                valueTextSize = 1f
                valueTextColor = Color.BLACK
            }

            barChart.data = BarData(barDataSet)
            barChart.invalidate()

            binding.textTotalbalance.text = state.balanceText
            binding.textTotalinflow.text = state.inflowText
            binding.textTotaloutflow.text = state.outflowText
        }

        barChart.apply {
            setFitBars(true)
            description.isEnabled = false
            animateY(1000)
            invalidate()
        }
    }

}
