package ua.smartmir.picblend.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import ua.smartmir.picblend.base.BaseViewModel
import ua.smartmir.picblend.navigation.Navigator
import ua.smartmir.picblend.navigation.Screens
import ua.smartmir.picblend.ui.common.BarIconState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainState>(MainState()) {

    fun updateCurrentScreen(screen: Screens) = {
        _uiState.update {
            it.copy(
                currentScreen = screen,
                title = (screen as Screens.BaseScreens).titleStringId,
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