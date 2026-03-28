package com.cloudbudget.app.data.api

import com.cloudbudget.app.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CloudBudgetApi {

    @GET("/api/dashboard")
    suspend fun getDashboard(
        @Query("period") period: String? = null
    ): Response<DashboardResponse>

    @GET("/api/budget")
    suspend fun getBudget(): Response<BudgetResponse>

    @POST("/api/budget")
    suspend fun setBudget(
        @Body request: BudgetSetRequest
    ): Response<BudgetResponse>

    @GET("/api/waste")
    suspend fun getWaste(): Response<WasteResponse>

    @GET("/api/trends")
    suspend fun getTrends(
        @Query("days") days: Int = 7
    ): Response<TrendsResponse>
}
