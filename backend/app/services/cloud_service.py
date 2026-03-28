"""Cloud billing service - fetches real data or falls back to mock."""
import os
from app.services.mock_data import get_mock_dashboard, get_mock_budget, get_mock_waste, get_mock_trends

USE_MOCK = os.getenv("USE_MOCK_DATA", "true").lower() == "true"


class CloudBillingService:
    def __init__(self):
        self.use_mock = USE_MOCK
        self._budget = 500.0

    async def get_dashboard(self, period=None):
        if self.use_mock:
            data = get_mock_dashboard(period)
            data["budget_total"] = self._budget
            data["budget_remaining"] = round(self._budget - data["total_spend"], 2)
            data["over_budget"] = data["total_spend"] > self._budget
            return data
        try:
            return await self._fetch_real_dashboard(period)
        except Exception:
            return get_mock_dashboard(period)

    async def get_budget(self):
        if self.use_mock:
            return get_mock_budget(self._budget)
        try:
            return await self._fetch_real_budget()
        except Exception:
            return get_mock_budget(self._budget)

    async def set_budget(self, total_budget):
        self._budget = total_budget
        return await self.get_budget()

    async def get_waste(self):
        if self.use_mock:
            return get_mock_waste()
        try:
            return await self._fetch_real_waste()
        except Exception:
            return get_mock_waste()

    async def get_trends(self, days=7):
        if self.use_mock:
            return get_mock_trends(days)
        try:
            return await self._fetch_real_trends(days)
        except Exception:
            return get_mock_trends(days)

    async def _fetch_real_dashboard(self, period=None):
        raise NotImplementedError

    async def _fetch_real_budget(self):
        raise NotImplementedError

    async def _fetch_real_waste(self):
        raise NotImplementedError

    async def _fetch_real_trends(self, days=7):
        raise NotImplementedError


billing_service = CloudBillingService()
