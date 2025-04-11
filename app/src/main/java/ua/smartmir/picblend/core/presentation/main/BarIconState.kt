package ua.smartmir.picblend.core.presentation.main

import androidx.compose.ui.graphics.vector.ImageVector

data class BarIconState(
    val imageVector: ImageVector,
    val onClick: () -> Unit
)