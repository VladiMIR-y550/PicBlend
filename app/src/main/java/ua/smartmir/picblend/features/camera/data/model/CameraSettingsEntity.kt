package ua.smartmir.picblend.features.camera.data.model

import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import ua.smartmir.picblend.features.camera.domain.model.CameraSettings
import ua.smartmir.picblend.features.camera.presentation.Focal

data class CameraSettingsEntity(
    val cameraId: String,
    @StringRes val lensFacingLabel: Int,
    val lensFacingId: Int,
    val cameraSelector: CameraSelector,
    val focal: Focal,
    val isSelected: Boolean = false,
    val description: String = "",
)

fun CameraSettingsEntity.mapTo(): CameraSettings = CameraSettings(
    cameraId = cameraId,
    lensFacingLabel = lensFacingLabel,
    lensFacingId = lensFacingId,
    focal = focal,
    isSelected = isSelected,
    description = description
)