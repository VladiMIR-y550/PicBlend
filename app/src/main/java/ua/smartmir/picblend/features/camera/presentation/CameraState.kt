package ua.smartmir.picblend.features.camera.presentation

import android.graphics.Bitmap
import android.net.Uri
import ua.smartmir.picblend.base.ScreenState

data class CameraState(
    val image: Bitmap? = null,
    val filterList: List<FilterStateEntity> = emptyList(),
    val lastImageUri: Uri? = null,
    val isPhotoFiltersShowing: Boolean = false,
) : ScreenState