package ua.smartmir.picblend.common

import androidx.annotation.DrawableRes

data class BarIconState(
    @DrawableRes val imageVectorId: Int,
    val onClick: () -> Unit
)