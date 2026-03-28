from fastapi import APIRouter
from app.models.schemas import BudgetSetRequest
from app.services.cloud_service import billing_service

router = APIRouter()

@router.get("/budget")
async def get_budget():
    return await billing_service.get_budget()

@router.post("/budget")
async def set_budget(request: BudgetSetRequest):
    return await billing_service.set_budget(request.total_budget)
