package ua.smartmir.picblend.features.camera.presentation

import android.net.Uri
import androidx.compose.runtime.Immutable
import ua.smartmir.picblend.core.base.ScreenState
import ua.smartmir.picblend.core.presentation.StableBitmap
import ua.smartmir.picblend.features.camera.presentation.model.CameraSettingsUi
import ua.smartmir.picblend.features.camera.presentation.model.FilterUiState

@Immutable
data class CameraState(
    val image: StableBitmap? = null,
    val filterList: List<FilterUiState> = emptyList(),
    val lastImageUri: Uri? = null,
    val isPhotoFiltersShowing: Boolean = false,
    val cameras: List<CameraSettingsUi> = emptyList(),
) : ScreenState