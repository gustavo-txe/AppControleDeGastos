package com.example.controledegastos.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.controledegastos.ui.features.months.FilterMonthActivity
import com.example.controledegastos.R
import com.example.controledegastos.databinding.AdapterlayoutmonthBinding
import com.example.controledegastos.data.usecase.MonthTotalValue
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

class MyAdapterMonth @Inject constructor(
    private val context: Context,
    val list: List<String>,
    private val monthTotalValue: MonthTotalValue) : RecyclerView.Adapter<MyAdapterMonth.Mvh>() {

    class Mvh(binding: AdapterlayoutmonthBinding) : RecyclerView.ViewHolder(binding.root) {

        val inflow = binding.textInflow
        val outflow = binding.textOutflow
        val balance = binding.textBalance
        val month = binding.textViewMonth
        val pieChart = binding.pieChart

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Mvh {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapterlayoutmonth, parent, false)
        return Mvh(AdapterlayoutmonthBinding.bind(view))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Mvh, position: Int) {

        val month = list[position]

        CoroutineScope(Dispatchers.Main).launch {

            val inflow = monthTotalValue.inflow(position.toString())
            val outflow = monthTotalValue.totalOutflow(position.toString())
            val balance = monthTotalValue.totalBalance(position.toString())
            val totalBalancePie = monthTotalValue.totalBalancePie(position.toString())
            val outflowTotalPie = monthTotalValue.outflowTotalPie(position.toString())

            holder.inflow.text = NumberFormat.getCurrencyInstance().format(inflow)
            holder.outflow.text = NumberFormat.getCurrencyInstance().format(outflow)
            holder.balance.text = NumberFormat.getCurrencyInstance().format(balance)
            holder.month.text = month

            holder.itemView.setOnClickListener {

                if (balance == 0.00 && outflow == 0.00) {
                    Toast.makeText(context, "Nenhum dado encontrado.", Toast.LENGTH_SHORT).show()
                } else {
                    context.startActivity(
                        Intent(context, FilterMonthActivity::class.java)
                            .putExtra("month", position.toString())
                    )
                    (context as? Activity)?.finish()
                }
            }

            var inflowPie = totalBalancePie - outflowTotalPie
            if (outflowTotalPie.equals(0f) && inflowPie.equals(0f)) {
                inflowPie = 1f
            }
            val entries: ArrayList<PieEntry> = ArrayList()
            entries.add(PieEntry(inflowPie, "Entrada"))
            entries.add(PieEntry(-outflowTotalPie, "Saída"))

            val pieDataSet = PieDataSet(entries, "")
            pieDataSet.setColors(Color.parseColor("#4CAF50"), Color.parseColor("#DF4646"))
            pieDataSet.valueTextSize = 1f
            pieDataSet.valueTextColor = Color.WHITE

            val pieData = PieData(pieDataSet)
            holder.pieChart.data = pieData
            holder.pieChart.description.isEnabled = false
            holder.pieChart.animateY(1000)
            holder.pieChart.invalidate()
        }

    }
}








