package ua.smartmir.picblend.features.camera.presentation.model

import android.content.Context
import androidx.annotation.StringRes
import ua.smartmir.picblend.R
import ua.smartmir.picblend.features.camera.domain.model.CameraSettings
import ua.smartmir.picblend.features.camera.presentation.Focal

data class CameraSettingsUi(
    val cameraId: String,
    @StringRes val lensFacingLabel: Int,
    val lensFacingId: Int,
    val focal: Focal,
    val isSelected: Boolean = false,
    val description: String = ""
)

fun CameraSettings.mapTo(context: Context): CameraSettingsUi = with(context) {
    CameraSettingsUi(
        cameraId = cameraId,
        lensFacingLabel = lensFacingLabel,
        lensFacingId = lensFacingId,
        focal = focal,
        isSelected = isSelected,
        description = getString(
            R.string.camera_description,
            cameraId,
            getString(lensFacingLabel),
            getString(focal.cameraType),
            focal.focalValue?.toString() ?: getString(R.string.dots)
        )
    )
}