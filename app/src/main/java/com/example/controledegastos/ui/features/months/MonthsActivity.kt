package com.example.controledegastos.ui.features.months

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controledegastos.data.usecase.MonthTotalValue
import com.example.controledegastos.ui.adapter.MyAdapterMonth
import com.example.controledegastos.databinding.ActivityAllMonthsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MonthsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllMonthsBinding
    private lateinit var adapter: MyAdapterMonth

    @Inject
    lateinit var monthTotalValue: MonthTotalValue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllMonthsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val months = listOf(
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro")

        adapter = MyAdapterMonth(this@MonthsActivity, months, monthTotalValue)
        binding.recyclerViewMonths.layoutManager = LinearLayoutManager(this@MonthsActivity)
        binding.recyclerViewMonths.adapter = adapter

        binding.backIconMonth.setOnClickListener {
            finish()
        }

    }

}