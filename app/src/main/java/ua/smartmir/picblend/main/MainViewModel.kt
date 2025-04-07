package ua.smartmir.picblend.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.smartmir.picblend.common.BarIconState
import ua.smartmir.picblend.navigation.Navigator
import ua.smartmir.picblend.navigation.Screens
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private var _uiState = MutableStateFlow<MainState>(MainState())
    val uiState get() = _uiState.asStateFlow()

    fun updateCurrentScreen(screen: Screens) = {
        _uiState.update {
            it.copy(
                currentScreen = screen,
                barIcons = listOf(),
                onBackPressure = null
            )
        }
    }

    fun updateBarIcons(barIcons: List<BarIconState>) {
        _uiState.update {
            it.copy(barIcons = barIcons)
        }
    }

    fun onBackPressure(navigator: Navigator) {
        if (uiState.value.onBackPressure == null) {
            navigator.popBackStack()
        } else {
            uiState.value.onBackPressure?.invoke()
        }
    }
}