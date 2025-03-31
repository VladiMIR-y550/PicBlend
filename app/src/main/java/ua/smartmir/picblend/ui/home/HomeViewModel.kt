package ua.smartmir.picblend.ui.home

import android.util.Log
import kotlinx.coroutines.flow.update
import ua.smartmir.picblend.base.BaseViewModel

class HomeViewModel() : BaseViewModel<HomeState>(HomeState()) {

    fun launchRequestPermission() {
        _uiState.update {
            it.copy(isPermissionNeeded = true)
        }
    }

    fun resetPermissionNeededState() {
        _uiState.update {
            it.copy(isPermissionNeeded = false)
        }
    }
}