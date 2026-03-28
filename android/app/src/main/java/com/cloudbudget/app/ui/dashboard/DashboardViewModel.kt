package com.cloudbudget.app.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudbudget.app.data.model.DashboardResponse
import com.cloudbudget.app.data.repository.CloudBudgetRepository
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val repository = CloudBudgetRepository()

    private val _dashboard = MutableLiveData<DashboardResponse>()
    val dashboard: LiveData<DashboardResponse> = _dashboard

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loadDashboard() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getDashboard()
            result.onSuccess { _dashboard.value = it }
            result.onFailure { _error.value = it.message }
            _loading.value = false
        }
    }
}
