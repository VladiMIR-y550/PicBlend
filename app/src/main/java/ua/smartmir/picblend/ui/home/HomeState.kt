package ua.smartmir.picblend.ui.home

import androidx.compose.ui.graphics.ImageBitmap
import ua.smartmir.picblend.base.ScreenState

data class HomeState(
    val image: ImageBitmap? = null
) : ScreenState
