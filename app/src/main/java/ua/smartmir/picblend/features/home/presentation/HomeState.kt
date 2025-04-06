package ua.smartmir.picblend.features.home.presentation

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import ua.smartmir.picblend.base.ScreenState

data class HomeState(
    val image: ImageBitmap? = null,
    val isPermissionNeeded: Boolean = false,
    val bitmap: Bitmap? = null
) : ScreenState
