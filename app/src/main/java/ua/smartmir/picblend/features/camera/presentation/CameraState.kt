package ua.smartmir.picblend.features.camera.presentation

import android.graphics.Bitmap
import android.net.Uri
import ua.smartmir.picblend.core.base.ScreenState
import ua.smartmir.picblend.features.camera.presentation.model.CameraSettingsUi
import ua.smartmir.picblend.features.camera.presentation.model.FilterUiState

data class CameraState(
    val image: Bitmap? = null,
    val filterList: List<FilterUiState> = emptyList(),
    val lastImageUri: Uri? = null,
    val isPhotoFiltersShowing: Boolean = false,
    val cameras: List<CameraSettingsUi> = emptyList(),
) : ScreenState