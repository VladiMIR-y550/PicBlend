package ua.smartmir.picblend.core.presentation.main

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ua.smartmir.picblend.R
import ua.smartmir.picblend.core.base.ScreenState
import ua.smartmir.picblend.core.presentation.navigation.Screens

@Immutable
data class MainState(
    val currentScreen: Screens = Screens.Home,
    @StringRes val title: Int = R.string.app_name,
    val barIcons: List<BarIconState> = emptyList(),
    val showToolbar: Boolean = true,
    val onBackPressure: (() -> Unit)? = null,
) : ScreenState