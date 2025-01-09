package com.example.quotestable.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.NetworkMonitor
import com.example.quotes.QuotesTableView
import kotlinx.coroutines.launch

@Composable
fun AppContent(networkMonitor: NetworkMonitor) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val isConnected = remember { mutableStateOf(true) }

    LaunchedEffect(networkMonitor) {
        networkMonitor.networkStatus().collect { status ->
            isConnected.value = status
            if (!status) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Lost internet connection",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            } else {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier.fillMaxWidth()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                QuotesTableView()
            }
        }
    )
}