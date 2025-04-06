package ua.smartmir.picblend.features.camera.presentation

import android.graphics.Bitmap
import android.net.Uri
import ua.smartmir.picblend.base.ScreenState

data class CameraState(
    val lastImageUri: Uri? = null,
    val mainPreviewBitmap: Bitmap? = null,
    val isPhotoFiltersShowing: Boolean = false,
    val filterList: List<FilterStateEntity> = emptyList()
) : ScreenState