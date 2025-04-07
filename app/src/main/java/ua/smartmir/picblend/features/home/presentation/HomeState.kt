package ua.smartmir.picblend.features.home.presentation

import androidx.compose.ui.graphics.ImageBitmap
import ua.smartmir.picblend.base.ScreenState
import ua.smartmir.picblend.features.camera.presentation.FilterStateEntity

data class HomeState(
    val image: ImageBitmap? = null,
    val filterList: List<FilterStateEntity> = emptyList(),
    val isPermissionNeeded: Boolean = false,
    val isPhotoFiltersShowing: Boolean = false,
) : ScreenState
