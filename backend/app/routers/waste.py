from fastapi import APIRouter
from app.services.cloud_service import billing_service

router = APIRouter()

@router.get("/waste")
async def get_waste():
    return await billing_service.get_waste()
