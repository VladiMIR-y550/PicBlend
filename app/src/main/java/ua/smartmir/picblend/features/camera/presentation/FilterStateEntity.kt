package ua.smartmir.picblend.features.camera.presentation

import android.graphics.Bitmap
import ua.smartmir.picblend.common.filters.domain.model.FilterType
import ua.smartmir.picblend.common.filters.domain.model.FilterDomainEntity

data class FilterStateEntity(
    val filterType: FilterType,
    val name: String = filterType.toString(),
    val isSelected: Boolean = false,
    val filteredBitmap: Bitmap? = null
)

fun FilterDomainEntity.mapToStateEntity(): FilterStateEntity = FilterStateEntity(
    filterType = filterType,
    name = name,
    isSelected = isSelected,
    filteredBitmap = filteredImageData
)
