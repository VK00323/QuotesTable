package com.example.quotestable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _networkState = MutableStateFlow(true)
    val networkState = _networkState.asStateFlow()

    init {
        networkMonitoring()
    }

    private fun networkMonitoring() {
        viewModelScope.launch {
            networkMonitor.networkStatus().collect { isConnected ->
                _networkState.emit(isConnected)
            }
        }
    }
}