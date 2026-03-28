package com.cloudbudget.app.ui.trends

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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class TrendsFragment : Fragment() {

    private val viewModel: TrendsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineChart = view.findViewById<LineChart>(R.id.lineChart)
        val tvAvgDaily = view.findViewById<TextView>(R.id.tvAvgDaily)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        setupLineChart(lineChart)

        viewModel.trends.observe(viewLifecycleOwner) { data ->
            tvAvgDaily.text = "Avg daily: $${String.format("%.2f", data.avg_daily_total)}"

            val awsEntries = mutableListOf<Entry>()
            val azureEntries = mutableListOf<Entry>()
            val gcpEntries = mutableListOf<Entry>()
            val labels = mutableListOf<String>()

            data.daily_spends.forEachIndexed { i, spend ->
                awsEntries.add(Entry(i.toFloat(), spend.aws.toFloat()))
                azureEntries.add(Entry(i.toFloat(), spend.azure.toFloat()))
                gcpEntries.add(Entry(i.toFloat(), spend.gcp.toFloat()))
                labels.add(spend.date.takeLast(5)) // MM-DD
            }

            val awsDataSet = createLineDataSet(awsEntries, "AWS", "#FF9900")
            val azureDataSet = createLineDataSet(azureEntries, "Azure", "#0078D4")
            val gcpDataSet = createLineDataSet(gcpEntries, "GCP", "#4285F4")

            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            lineChart.data = LineData(awsDataSet, azureDataSet, gcpDataSet)
            lineChart.animateX(1000)
            lineChart.invalidate()
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            swipeRefresh.isRefreshing = loading
        }

        swipeRefresh.setOnRefreshListener { viewModel.loadTrends() }
        viewModel.loadTrends()
    }

    private fun setupLineChart(chart: LineChart) {
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setBackgroundColor(Color.parseColor("#1A1A2E"))
            legend.textColor = Color.WHITE
            legend.textSize = 12f
            xAxis.apply {
                textColor = Color.WHITE
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
            }
            axisLeft.textColor = Color.WHITE
            axisRight.isEnabled = false
        }
    }

    private fun createLineDataSet(entries: List<Entry>, label: String, colorHex: String): LineDataSet {
        return LineDataSet(entries, label).apply {
            color = Color.parseColor(colorHex)
            setCircleColor(Color.parseColor(colorHex))
            lineWidth = 2.5f
            circleRadius = 4f
            valueTextColor = Color.WHITE
            valueTextSize = 10f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = Color.parseColor(colorHex)
            fillAlpha = 30
        }
    }
}
