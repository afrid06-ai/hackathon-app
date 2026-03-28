"""Mock data service - realistic demo data when cloud credentials unavailable."""
import random
from datetime import datetime, timedelta


def get_mock_dashboard(period=None):
    if not period:
        period = datetime.now().strftime("%Y-%m")
    aws = round(random.uniform(120, 180), 2)
    azure = round(random.uniform(80, 130), 2)
    gcp = round(random.uniform(60, 100), 2)
    total = round(aws + azure + gcp, 2)
    budget = 500.0
    return {
        "total_spend": total, "currency": "USD", "period": period,
        "breakdown": [
            {"provider": "aws", "amount": aws, "currency": "USD", "period": period},
            {"provider": "azure", "amount": azure, "currency": "USD", "period": period},
            {"provider": "gcp", "amount": gcp, "currency": "USD", "period": period},
        ],
        "budget_total": budget,
        "budget_remaining": round(budget - total, 2),
        "over_budget": total > budget,
    }


def get_mock_budget(total_budget=500.0):
    ratios = {"aws": 0.45, "azure": 0.30, "gcp": 0.25}
    allocations = []
    for provider, ratio in ratios.items():
        allocated = round(total_budget * ratio, 2)
        actual = round(random.uniform(allocated * 0.6, allocated * 1.15), 2)
        pct = round((actual / allocated) * 100, 1) if allocated > 0 else 0
        allocations.append({
            "provider": provider, "allocated": allocated,
            "actual": actual, "percentage_used": pct,
            "over_budget": actual > allocated,
        })
    return {"total_budget": total_budget, "currency": "USD", "allocations": allocations}


def get_mock_waste():
    items = [
        {"provider": "aws", "resource_type": "EC2 Instance", "resource_id": "i-0a1b2c3d4e5f67890",
         "resource_name": "dev-test-server", "monthly_cost": 45.60,
         "waste_reason": "Running 24/7 with <5% CPU utilization",
         "recommendation": "Stop or downgrade to t3.micro"},
        {"provider": "aws", "resource_type": "EBS Volume", "resource_id": "vol-0123456789abcdef0",
         "resource_name": "unattached-volume-old", "monthly_cost": 12.80,
         "waste_reason": "Unattached for 30+ days",
         "recommendation": "Snapshot and delete"},
        {"provider": "azure", "resource_type": "Virtual Machine", "resource_id": "vm-staging-westus",
         "resource_name": "staging-vm-westus", "monthly_cost": 38.20,
         "waste_reason": "Idle for 14 days, no SSH connections",
         "recommendation": "Deallocate or set auto-shutdown"},
        {"provider": "azure", "resource_type": "Storage Account", "resource_id": "oldbackupstorage",
         "resource_name": "oldbackupstorage2024", "monthly_cost": 8.50,
         "waste_reason": "No access in 60+ days",
         "recommendation": "Move to cool/archive tier or delete"},
        {"provider": "gcp", "resource_type": "Compute Instance", "resource_id": "test-vm-central",
         "resource_name": "test-vm-central", "monthly_cost": 28.90,
         "waste_reason": "Zero network traffic for 21 days",
         "recommendation": "Stop or use preemptible VM"},
        {"provider": "gcp", "resource_type": "Persistent Disk", "resource_id": "orphan-disk",
         "resource_name": "orphaned-disk-snapshot", "monthly_cost": 6.40,
         "waste_reason": "Not attached to any instance",
         "recommendation": "Delete after verifying no data needed"},
    ]
    return {"total_waste": round(sum(i["monthly_cost"] for i in items), 2), "currency": "USD", "items": items}


def get_mock_trends(days=7):
    today = datetime.now().date()
    daily = []
    for i in range(days - 1, -1, -1):
        d = today - timedelta(days=i)
        aws = round(random.uniform(4.0, 8.0), 2)
        azure = round(random.uniform(2.5, 5.5), 2)
        gcp = round(random.uniform(1.5, 4.0), 2)
        daily.append({"date": d.isoformat(), "aws": aws, "azure": azure, "gcp": gcp, "total": round(aws + azure + gcp, 2)})
    avg = round(sum(d["total"] for d in daily) / len(daily), 2)
    return {"period_days": days, "daily_spends": daily, "avg_daily_total": avg}
