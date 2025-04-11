package ua.smartmir.picblend.features.home.presentation

import android.net.Uri
import androidx.compose.runtime.Immutable
import ua.smartmir.picblend.core.base.ScreenState
import ua.smartmir.picblend.core.presentation.StableBitmap
import ua.smartmir.picblend.features.camera.presentation.model.FilterUiState

@Immutable
data class HomeState(
    val image: StableBitmap? = null,
    val uriCashedImage: Uri? = null,
    val filterList: List<FilterUiState> = emptyList(),
    val isPermissionNeeded: Boolean = false,
    val isPhotoFiltersShowing: Boolean = false,
) : ScreenState
