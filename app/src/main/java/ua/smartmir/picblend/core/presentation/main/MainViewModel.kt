package ua.smartmir.picblend.core.presentation.main

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import ua.smartmir.picblend.core.base.BaseViewModel
import ua.smartmir.picblend.core.base.UiEffect
import ua.smartmir.picblend.core.presentation.navigation.Navigator
import ua.smartmir.picblend.core.presentation.navigation.Screens
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainState, UiEffect>(MainState()) {

    fun updateCurrentScreen(screen: Screens) = {
        _uiState.update {
            it.copy(
                currentScreen = screen,
                barIcons = listOf(),
                onBackPressure = null
            )
        }
    }

    fun updateShowToolbar(showToolbar: Boolean) {
        _uiState.update {
            it.copy(showToolbar = showToolbar)
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