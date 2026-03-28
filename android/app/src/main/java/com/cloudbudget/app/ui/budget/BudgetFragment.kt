package com.cloudbudget.app.ui.budget

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloudbudget.app.R

class BudgetFragment : Fragment() {

    private val viewModel: BudgetViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_budget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTotalBudget = view.findViewById<TextView>(R.id.tvTotalBudget)
        val etBudget = view.findViewById<EditText>(R.id.etBudget)
        val btnSetBudget = view.findViewById<Button>(R.id.btnSetBudget)
        val llAllocations = view.findViewById<LinearLayout>(R.id.llAllocations)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        viewModel.budget.observe(viewLifecycleOwner) { data ->
            tvTotalBudget.text = "Total Budget: $${String.format("%.2f", data.total_budget)}"
            llAllocations.removeAllViews()

            data.allocations.forEach { alloc ->
                val itemView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_budget_allocation, llAllocations, false)

                val tvProvider = itemView.findViewById<TextView>(R.id.tvProvider)
                val tvAllocated = itemView.findViewById<TextView>(R.id.tvAllocated)
                val tvActual = itemView.findViewById<TextView>(R.id.tvActual)
                val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
                val tvPercentage = itemView.findViewById<TextView>(R.id.tvPercentage)

                tvProvider.text = alloc.provider.uppercase()
                tvAllocated.text = "Allocated: $${String.format("%.2f", alloc.allocated)}"
                tvActual.text = "Actual: $${String.format("%.2f", alloc.actual)}"
                progressBar.progress = alloc.percentage_used.toInt().coerceAtMost(100)
                tvPercentage.text = "${String.format("%.1f", alloc.percentage_used)}%"

                if (alloc.over_budget) {
                    tvPercentage.setTextColor(Color.parseColor("#FF5252"))
                    tvActual.setTextColor(Color.parseColor("#FF5252"))
                } else {
                    tvPercentage.setTextColor(Color.parseColor("#69F0AE"))
                    tvActual.setTextColor(Color.parseColor("#69F0AE"))
                }

                llAllocations.addView(itemView)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            swipeRefresh.isRefreshing = loading
        }

        btnSetBudget.setOnClickListener {
            val amount = etBudget.text.toString().toDoubleOrNull()
            if (amount != null && amount > 0) {
                viewModel.setBudget(amount)
                etBudget.text.clear()
            } else {
                Toast.makeText(requireContext(), "Enter a valid budget amount", Toast.LENGTH_SHORT).show()
            }
        }

        swipeRefresh.setOnRefreshListener { viewModel.loadBudget() }
        viewModel.loadBudget()
    }
}
