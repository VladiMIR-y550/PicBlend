package ua.smartmir.picblend.features.home.presentation

import android.graphics.Bitmap
import android.net.Uri
import ua.smartmir.picblend.base.ScreenState
import ua.smartmir.picblend.features.camera.presentation.FilterStateEntity

data class HomeState(
    val image: Bitmap? = null,
    val uriCashedImage: Uri? = null,
    val filterList: List<FilterStateEntity> = emptyList(),
    val isPermissionNeeded: Boolean = false,
    val isPhotoFiltersShowing: Boolean = false,
) : ScreenState
