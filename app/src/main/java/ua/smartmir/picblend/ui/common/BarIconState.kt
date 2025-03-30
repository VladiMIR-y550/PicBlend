package ua.smartmir.picblend.ui.common

import androidx.annotation.DrawableRes

data class BarIconState(
    @DrawableRes val imageVectorId: Int,
    val onClick: () -> Unit
)