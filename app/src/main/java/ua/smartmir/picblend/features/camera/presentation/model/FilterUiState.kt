package ua.smartmir.picblend.features.camera.presentation.model

import android.graphics.Bitmap
import ua.smartmir.picblend.features.filters.domain.model.FilterType
import ua.smartmir.picblend.features.filters.domain.model.Filter

data class FilterUiState(
    val filterType: FilterType,
    val name: String = filterType.toString(),
    val isSelected: Boolean = false,
    val filteredBitmap: Bitmap? = null
)

fun Filter.mapToStateEntity(): FilterUiState = FilterUiState(
    filterType = filterType,
    name = name,
    isSelected = isSelected,
    filteredBitmap = filteredImageData
)
