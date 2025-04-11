package ua.smartmir.picblend.core.presentation.main

import androidx.annotation.StringRes
import ua.smartmir.picblend.R
import ua.smartmir.picblend.core.base.ScreenState
import ua.smartmir.picblend.core.presentation.navigation.Screens

data class MainState(
    val currentScreen: Screens = Screens.Home,
    @StringRes val title: Int = R.string.home,
    val barIcons: List<BarIconState> = emptyList(),
    val onBackPressure: (() -> Unit)? = null,
) : ScreenState