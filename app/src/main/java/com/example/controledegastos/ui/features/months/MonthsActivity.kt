package com.example.controledegastos.ui.features.months

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controledegastos.databinding.ActivityAllMonthsBinding
import com.example.controledegastos.ui.adapter.MyAdapterMonth
import com.example.controledegastos.viewmodel.MonthsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllMonthsBinding
    private lateinit var adapter: MyAdapterMonth

    private val monthsViewModel: MonthsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllMonthsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val months = listOf(
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        )

        adapter = MyAdapterMonth { monthSummary ->
            if (!monthSummary.hasData) {
                Toast.makeText(this, "Nenhum dado encontrado.", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(
                    Intent(this, FilterMonthActivity::class.java)
                        .putExtra("month", monthSummary.monthIndex.toString())
                )
                finish()
            }
        }

        binding.recyclerViewMonths.layoutManager = LinearLayoutManager(this@MonthsActivity)
        binding.recyclerViewMonths.adapter = adapter

        monthsViewModel.months.observe(this) {
            adapter.setItems(it)
        }
        monthsViewModel.loadMonths(months)

        binding.backIconMonth.setOnClickListener {
            finish()
        }

    }

}
