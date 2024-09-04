package com.lonwulf.core.utils

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object Complete : UiState()
    object Error : UiState()
}
