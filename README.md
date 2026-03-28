# CloudBudget - Multi-Cloud Billing Tracker

> One app. Three clouds. Zero billing surprises.

## Quick Start

### Backend
```bash
cd backend && pip install -r requirements.txt && uvicorn app.main:app --reload --port 8000
```

### Android
Open android/ in Android Studio, update BASE_URL in RetrofitClient.kt, build and run.

## Features
- Unified Dashboard with pie chart
- Auto Budget Allocation across clouds
- Over-Budget Push Notifications
- Waste Detector for idle resources
- 7-Day Spend Trend Graph
- Mock Data Fallback for demos
- Dark Mode Material Design 3 UI

## Tech Stack
Backend: Python, FastAPI, Uvicorn | Android: Kotlin, Retrofit2, MPAndroidChart, Material3
