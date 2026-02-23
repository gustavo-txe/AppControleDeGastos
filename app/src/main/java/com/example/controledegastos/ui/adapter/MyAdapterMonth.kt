package com.example.controledegastos.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controledegastos.R
import com.example.controledegastos.databinding.AdapterlayoutmonthBinding
import com.example.controledegastos.ui.model.MonthSummaryUi
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MyAdapterMonth(
    private val onMonthClicked: (MonthSummaryUi) -> Unit
) : RecyclerView.Adapter<MyAdapterMonth.Mvh>() {

    private var list: List<MonthSummaryUi> = emptyList()

    class Mvh(binding: AdapterlayoutmonthBinding) : RecyclerView.ViewHolder(binding.root) {
        val inflow = binding.textInflow
        val outflow = binding.textOutflow
        val balance = binding.textBalance
        val month = binding.textViewMonth
        val pieChart = binding.pieChart
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Mvh {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapterlayoutmonth, parent, false)
        return Mvh(AdapterlayoutmonthBinding.bind(view))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Mvh, position: Int) {
        val monthSummary = list[position]

        holder.inflow.text = monthSummary.inflowText
        holder.outflow.text = monthSummary.outflowText
        holder.balance.text = monthSummary.balanceText
        holder.month.text = monthSummary.monthName

        val entries = arrayListOf(
            PieEntry(monthSummary.inflowPie, "Entrada"),
            PieEntry(monthSummary.outflowPie, "Saída")
        )

        val pieDataSet = PieDataSet(entries, "").apply {
            setColors(Color.parseColor("#4CAF50"), Color.parseColor("#DF4646"))
            valueTextSize = 1f
            valueTextColor = Color.WHITE
        }

        holder.pieChart.data = PieData(pieDataSet)
        holder.pieChart.description.isEnabled = false
        holder.pieChart.animateY(1000)
        holder.pieChart.invalidate()

        holder.itemView.setOnClickListener { onMonthClicked(monthSummary) }
    }

    fun setItems(newItems: List<MonthSummaryUi>) {
        list = newItems
        notifyDataSetChanged()
    }
}
