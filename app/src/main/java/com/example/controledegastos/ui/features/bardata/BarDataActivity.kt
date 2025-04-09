package com.example.controledegastos.ui.features.bardata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.controledegastos.databinding.ActivityGratBinding
import com.example.controledegastos.viewmodel.ItemsViewModel
import com.github.mikephil.charting.charts.BarChart
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

        itemsViewModel.barData()

        binding.barGrat.setOnLongClickListener {
            itemsViewModel.barData()
            true
        }

        setupObservers()
    }
    private fun setupObservers() {

        val barChart: BarChart = binding.barGrat

        itemsViewModel.barData.observe(this@BarDataActivity) {
            it?.let {
                barChart.data = it
            }
        }

        barChart.apply {
            setFitBars(true)
            description.isEnabled = false
            animateY(1000)
            invalidate()
        }

        itemsViewModel.balanceBardata.observe(this@BarDataActivity) {
            it?.let {
                binding.textTotalbalance.text = it
            }
        }
        itemsViewModel.inflowBardata.observe(this@BarDataActivity) {
            it?.let {
                binding.textTotalinflow.text = it
            }
        }
        itemsViewModel.outflowBardata.observe(this@BarDataActivity) {
            it?.let {
                binding.textTotaloutflow.text = it
            }
        }
    }

}





