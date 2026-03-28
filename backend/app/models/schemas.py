from pydantic import BaseModel
from typing import List, Optional
from datetime import date


class CloudSpend(BaseModel):
    provider: str
    amount: float
    currency: str = "USD"
    period: str


class DashboardResponse(BaseModel):
    total_spend: float
    currency: str = "USD"
    period: str
    breakdown: List[CloudSpend]
    budget_total: float
    budget_remaining: float
    over_budget: bool


class BudgetAllocation(BaseModel):
    provider: str
    allocated: float
    actual: float
    percentage_used: float
    over_budget: bool


class BudgetResponse(BaseModel):
    total_budget: float
    currency: str = "USD"
    allocations: List[BudgetAllocation]


class BudgetSetRequest(BaseModel):
    total_budget: float


class WasteItem(BaseModel):
    provider: str
    resource_type: str
    resource_id: str
    resource_name: str
    monthly_cost: float
    waste_reason: str
    recommendation: str


class WasteResponse(BaseModel):
    total_waste: float
    currency: str = "USD"
    items: List[WasteItem]


class DailySpend(BaseModel):
    date: str
    aws: float
    azure: float
    gcp: float
    total: float


class TrendsResponse(BaseModel):
    period_days: int
    daily_spends: List[DailySpend]
    avg_daily_total: float
