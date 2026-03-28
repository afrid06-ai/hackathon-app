from fastapi import APIRouter, Query
from app.services.cloud_service import billing_service

router = APIRouter()

@router.get("/trends")
async def get_trends(days: int = Query(7, ge=1, le=30, description="Number of days")):
    return await billing_service.get_trends(days)
