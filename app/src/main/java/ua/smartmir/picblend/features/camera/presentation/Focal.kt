package ua.smartmir.picblend.features.camera.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.ui.graphics.vector.ImageVector
import ua.smartmir.picblend.R
import ua.smartmir.picblend.features.camera.presentation.Focal.Standard
import ua.smartmir.picblend.features.camera.presentation.Focal.Telephoto
import ua.smartmir.picblend.features.camera.presentation.Focal.UltraWide
import ua.smartmir.picblend.features.camera.presentation.Focal.Unknown

interface Focal {
    val focalValue: Float?
    val cameraType: Int
    val imageVector: ImageVector

    abstract class Abstract(
        override val cameraType: Int,
        override val imageVector: ImageVector
    ) : Focal

    data class Unknown(override val focalValue: Float?) : Abstract(
        cameraType = R.string.camera_type_unknown,
        imageVector = Icons.Default.QuestionMark
    )

    data class UltraWide(override val focalValue: Float?) : Abstract(
        cameraType = R.string.camera_type_ultra_wide,
        imageVector = Icons.Default.NaturePeople
    )

    data class Standard(override val focalValue: Float?) : Abstract(
        cameraType = R.string.camera_type_standard,
        imageVector = Icons.Default.HowToReg
    )

    data class Telephoto(override val focalValue: Float?) : Abstract(
        cameraType = R.string.camera_type_telephoto,
        imageVector = Icons.Default.CrueltyFree
    )
}

fun Float?.mapTo(): Focal {
    return when {
        this == null -> Unknown(this)
        this < 2.0f -> UltraWide(this)
        this in 2.0f..4.5f -> Standard(this)
        this > 4.5f -> Telephoto(this)
        else -> Unknown(this)
    }
}