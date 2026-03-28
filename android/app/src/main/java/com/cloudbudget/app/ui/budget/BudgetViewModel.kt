package com.cloudbudget.app.ui.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudbudget.app.data.model.BudgetResponse
import com.cloudbudget.app.data.repository.CloudBudgetRepository
import kotlinx.coroutines.launch

class BudgetViewModel : ViewModel() {

    private val repository = CloudBudgetRepository()

    private val _budget = MutableLiveData<BudgetResponse>()
    val budget: LiveData<BudgetResponse> = _budget

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loadBudget() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getBudget()
            result.onSuccess { _budget.value = it }
            result.onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun setBudget(amount: Double) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.setBudget(amount)
            result.onSuccess { _budget.value = it }
            result.onFailure { _error.value = it.message }
            _loading.value = false
        }
    }
}
