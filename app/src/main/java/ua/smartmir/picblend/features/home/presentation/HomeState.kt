package ua.smartmir.picblend.features.home.presentation

import android.graphics.Bitmap
import android.net.Uri
import ua.smartmir.picblend.core.base.ScreenState
import ua.smartmir.picblend.features.camera.presentation.model.FilterUiState

data class HomeState(
    val image: Bitmap? = null,
    val uriCashedImage: Uri? = null,
    val filterList: List<FilterUiState> = emptyList(),
    val isPermissionNeeded: Boolean = false,
    val isPhotoFiltersShowing: Boolean = false,
) : ScreenState
