from fastapi import APIRouter, Query
from app.services.cloud_service import billing_service

router = APIRouter()

@router.get("/dashboard")
async def get_dashboard(period: str = Query(None, description="Billing period YYYY-MM")):
    return await billing_service.get_dashboard(period)
