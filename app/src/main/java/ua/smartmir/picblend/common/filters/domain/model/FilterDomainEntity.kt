package ua.smartmir.picblend.common.filters.domain.model

import android.graphics.Bitmap

data class FilterDomainEntity(
    val filterType: FilterType = FilterType.None,
    val name: String = filterType.toString(),
    val isSelected: Boolean = false,
    val filteredImageData: Bitmap? = null
)