package ua.smartmir.picblend.features.camera.presentation.model

import android.graphics.Bitmap
import androidx.compose.runtime.Stable
import ua.smartmir.picblend.features.filters.domain.model.Filter
import ua.smartmir.picblend.features.filters.domain.model.FilterType

@Stable
data class FilterUiState(
    val filterType: FilterType,
    val name: String = filterType.toString(),
    val isSelected: Boolean = false,
    val filteredBitmap: Bitmap? = null
)

fun Filter.mapTo(): FilterUiState = FilterUiState(
    filterType = filterType,
    name = name,
    isSelected = isSelected,
    filteredBitmap = filteredImageData
)
