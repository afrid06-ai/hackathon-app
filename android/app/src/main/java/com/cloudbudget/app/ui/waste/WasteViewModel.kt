package com.cloudbudget.app.ui.waste

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudbudget.app.data.model.WasteResponse
import com.cloudbudget.app.data.repository.CloudBudgetRepository
import kotlinx.coroutines.launch

class WasteViewModel : ViewModel() {

    private val repository = CloudBudgetRepository()

    private val _waste = MutableLiveData<WasteResponse>()
    val waste: LiveData<WasteResponse> = _waste

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loadWaste() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getWaste()
            result.onSuccess { _waste.value = it }
            result.onFailure { _error.value = it.message }
            _loading.value = false
        }
    }
}
