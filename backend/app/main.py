from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers import dashboard, budget, waste, trends

app = FastAPI(
    title="CloudBudget API",
    description="Multi-Cloud Billing Tracker Backend",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(dashboard.router, prefix="/api", tags=["Dashboard"])
app.include_router(budget.router, prefix="/api", tags=["Budget"])
app.include_router(waste.router, prefix="/api", tags=["Waste"])
app.include_router(trends.router, prefix="/api", tags=["Trends"])


@app.get("/")
def root():
    return {"message": "CloudBudget API is running", "version": "1.0.0"}


@app.get("/health")
def health_check():
    return {"status": "healthy"}
