package com.cloudbudget.app.ui.trends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudbudget.app.data.model.TrendsResponse
import com.cloudbudget.app.data.repository.CloudBudgetRepository
import kotlinx.coroutines.launch

class TrendsViewModel : ViewModel() {

    private val repository = CloudBudgetRepository()

    private val _trends = MutableLiveData<TrendsResponse>()
    val trends: LiveData<TrendsResponse> = _trends

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loadTrends(days: Int = 7) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getTrends(days)
            result.onSuccess { _trends.value = it }
            result.onFailure { _error.value = it.message }
            _loading.value = false
        }
    }
}
