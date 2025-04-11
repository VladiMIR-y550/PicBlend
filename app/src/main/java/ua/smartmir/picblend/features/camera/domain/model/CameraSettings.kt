package ua.smartmir.picblend.features.camera.domain.model

import androidx.annotation.StringRes
import ua.smartmir.picblend.features.camera.presentation.Focal

data class CameraSettings(
    val cameraId: String,
    @StringRes val lensFacingLabel: Int,
    val lensFacingId: Int,
    val focal: Focal,
    val isSelected: Boolean = false,
    val description: String = ""
)