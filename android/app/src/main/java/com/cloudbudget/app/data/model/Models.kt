package com.cloudbudget.app.data.model

data class DashboardResponse(
    val total_spend: Double,
    val currency: String,
    val period: String,
    val breakdown: List<CloudSpend>,
    val budget_total: Double,
    val budget_remaining: Double,
    val over_budget: Boolean
)

data class CloudSpend(
    val provider: String,
    val amount: Double,
    val currency: String,
    val period: String
)

data class BudgetResponse(
    val total_budget: Double,
    val currency: String,
    val allocations: List<BudgetAllocation>
)

data class BudgetAllocation(
    val provider: String,
    val allocated: Double,
    val actual: Double,
    val percentage_used: Double,
    val over_budget: Boolean
)

data class BudgetSetRequest(
    val total_budget: Double
)

data class WasteResponse(
    val total_waste: Double,
    val currency: String,
    val items: List<WasteItem>
)

data class WasteItem(
    val provider: String,
    val resource_type: String,
    val resource_id: String,
    val resource_name: String,
    val monthly_cost: Double,
    val waste_reason: String,
    val recommendation: String
)

data class TrendsResponse(
    val period_days: Int,
    val daily_spends: List<DailySpend>,
    val avg_daily_total: Double
)

data class DailySpend(
    val date: String,
    val aws: Double,
    val azure: Double,
    val gcp: Double,
    val total: Double
)
