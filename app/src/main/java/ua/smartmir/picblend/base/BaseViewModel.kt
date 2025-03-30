package ua.smartmir.picblend.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel<T : ScreenState>(
    state: T
) : ViewModel() {

    protected val _uiState = MutableStateFlow(state)
    val uiState: StateFlow<T> = _uiState.asStateFlow()
}