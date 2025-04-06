package ua.smartmir.picblend.features.camera.domain

import android.graphics.Bitmap
import ua.smartmir.picblend.features.camera.domain.FilterType

data class FilterDomainEntity(
    val filterType: FilterType = FilterType.None,
    val name: String = filterType.toString(),
    val isSelected: Boolean = false,
    val filteredImageData: Bitmap? = null
)