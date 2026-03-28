package com.cloudbudget.app.ui.waste

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloudbudget.app.R

class WasteFragment : Fragment() {

    private val viewModel: WasteViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_waste, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTotalWaste = view.findViewById<TextView>(R.id.tvTotalWaste)
        val llWasteItems = view.findViewById<LinearLayout>(R.id.llWasteItems)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        viewModel.waste.observe(viewLifecycleOwner) { data ->
            tvTotalWaste.text = "Total Waste: $${String.format("%.2f", data.total_waste)}/month"
            tvTotalWaste.setTextColor(Color.parseColor("#FF5252"))
            llWasteItems.removeAllViews()

            data.items.forEach { item ->
                val itemView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_waste, llWasteItems, false)

                itemView.findViewById<TextView>(R.id.tvResourceName).text = item.resource_name
                itemView.findViewById<TextView>(R.id.tvResourceType).text = "${item.provider.uppercase()} - ${item.resource_type}"
                itemView.findViewById<TextView>(R.id.tvMonthlyCost).text = "$${String.format("%.2f", item.monthly_cost)}/mo"
                itemView.findViewById<TextView>(R.id.tvWasteReason).text = item.waste_reason
                itemView.findViewById<TextView>(R.id.tvRecommendation).text = item.recommendation

                val providerColor = when (item.provider) {
                    "aws" -> Color.parseColor("#FF9900")
                    "azure" -> Color.parseColor("#0078D4")
                    "gcp" -> Color.parseColor("#4285F4")
                    else -> Color.WHITE
                }
                itemView.findViewById<TextView>(R.id.tvResourceType).setTextColor(providerColor)

                llWasteItems.addView(itemView)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            swipeRefresh.isRefreshing = loading
        }

        swipeRefresh.setOnRefreshListener { viewModel.loadWaste() }
        viewModel.loadWaste()
    }
}
