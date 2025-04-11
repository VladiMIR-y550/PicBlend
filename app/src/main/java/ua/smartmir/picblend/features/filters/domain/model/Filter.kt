package ua.smartmir.picblend.features.filters.domain.model

import android.graphics.Bitmap

data class Filter(
    val filterType: FilterType = FilterType.None,
    val name: String = filterType.toString(),
    val isSelected: Boolean = false,
    val filteredImageData: Bitmap? = null
)