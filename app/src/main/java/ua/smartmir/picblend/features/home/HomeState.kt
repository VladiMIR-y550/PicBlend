package ua.smartmir.picblend.features.home

import androidx.compose.ui.graphics.ImageBitmap
import ua.smartmir.picblend.base.ScreenState

data class HomeState(
    val image: ImageBitmap? = null,
    val isPermissionNeeded: Boolean = false
) : ScreenState
