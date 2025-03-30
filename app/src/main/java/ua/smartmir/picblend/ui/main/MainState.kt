package ua.smartmir.picblend.ui.main

import androidx.annotation.StringRes
import ua.smartmir.picblend.R
import ua.smartmir.picblend.base.ScreenState
import ua.smartmir.picblend.navigation.Screens
import ua.smartmir.picblend.ui.common.BarIconState

data class MainState(
    val currentScreen: Screens = Screens.Home,
    @StringRes val title: Int = R.string.home,
    val barIcons: List<BarIconState> = emptyList(),
    val onBackPressure: (() -> Unit)? = null,
) : ScreenState