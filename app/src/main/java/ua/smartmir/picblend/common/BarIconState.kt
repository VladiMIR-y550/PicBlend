package ua.smartmir.picblend.common

import androidx.compose.ui.graphics.vector.ImageVector

data class BarIconState(
    val imageVector: ImageVector,
    val onClick: () -> Unit
)