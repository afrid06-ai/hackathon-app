package com.cloudbudget.app.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloudbudget.app.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.animation.Easing

class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val tvTotalSpend = view.findViewById<TextView>(R.id.tvTotalSpend)
        val tvBudgetRemaining = view.findViewById<TextView>(R.id.tvBudgetRemaining)
        val tvPeriod = view.findViewById<TextView>(R.id.tvPeriod)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        setupPieChart(pieChart)

        viewModel.dashboard.observe(viewLifecycleOwner) { data ->
            tvTotalSpend.text = "$${String.format("%.2f", data.total_spend)}"
            tvBudgetRemaining.text = "Budget remaining: $${String.format("%.2f", data.budget_remaining)}"
            tvPeriod.text = data.period

            if (data.over_budget) {
                tvBudgetRemaining.setTextColor(Color.parseColor("#FF5252"))
                tvBudgetRemaining.text = "OVER BUDGET by $${String.format("%.2f", -data.budget_remaining)}"
            } else {
                tvBudgetRemaining.setTextColor(Color.parseColor("#69F0AE"))
            }

            val entries = data.breakdown.map { PieEntry(it.amount.toFloat(), it.provider.uppercase()) }
            val dataSet = PieDataSet(entries, "Cloud Spend").apply {
                colors = listOf(
                    Color.parseColor("#FF9900"),  // AWS Orange
                    Color.parseColor("#0078D4"),  // Azure Blue
                    Color.parseColor("#4285F4")   // GCP Blue
                )
                valueTextColor = Color.WHITE
                valueTextSize = 14f
            }
            pieChart.data = PieData(dataSet)
            pieChart.animateY(1000, Easing.EaseInOutQuad)
            pieChart.invalidate()
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            swipeRefresh.isRefreshing = loading
        }

        swipeRefresh.setOnRefreshListener { viewModel.loadDashboard() }

        viewModel.loadDashboard()
    }

    private fun setupPieChart(chart: PieChart) {
        chart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.parseColor("#1A1A2E"))
            holeRadius = 45f
            transparentCircleRadius = 50f
            setDrawCenterText(true)
            centerText = "Cloud\nSpend"
            setCenterTextColor(Color.WHITE)
            setCenterTextSize(16f)
            legend.textColor = Color.WHITE
            legend.textSize = 12f
            setEntryLabelColor(Color.WHITE)
        }
    }
}
