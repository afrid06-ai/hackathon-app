package com.cloudbudget.app.data.repository

import com.cloudbudget.app.data.api.RetrofitClient
import com.cloudbudget.app.data.model.*

class CloudBudgetRepository {

    private val api = RetrofitClient.api

    suspend fun getDashboard(period: String? = null): Result<DashboardResponse> {
        return try {
            val response = api.getDashboard(period)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBudget(): Result<BudgetResponse> {
        return try {
            val response = api.getBudget()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setBudget(totalBudget: Double): Result<BudgetResponse> {
        return try {
            val response = api.setBudget(BudgetSetRequest(totalBudget))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWaste(): Result<WasteResponse> {
        return try {
            val response = api.getWaste()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrends(days: Int = 7): Result<TrendsResponse> {
        return try {
            val response = api.getTrends(days)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
