package ua.smartmir.picblend.common.filters.domain.usecase

import android.graphics.Bitmap
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import ua.smartmir.picblend.common.filters.domain.repository.FiltersRepository
import ua.smartmir.picblend.common.filters.domain.model.FilterType
import ua.smartmir.picblend.common.filters.utils.FilterProcessor
import ua.smartmir.picblend.common.filters.utils.processFilteredBitmap
import ua.smartmir.picblend.common.filters.utils.processFilteredBitmapSingle

class ApplyFilterUseCase @AssistedInject constructor(
    @Assisted private val filtersRepository: FiltersRepository,
    private val filterProcessor: FilterProcessor
) {
    suspend fun applySelectedFilter(bitmap: Bitmap): Bitmap? {
        return processFilteredBitmapSingle(
            bitmap,
            filtersRepository.filterStatesFlow.value
        ) { image, filtersList ->
            val selected = filtersList.find { it.isSelected }
            if (selected != null && selected.filterType != FilterType.None) {
                filterProcessor.applyFilter(image, selected.filterType)
            } else {
                image
            }
        }
    }

    fun applySelectedFilter(bitmapFlow: Flow<Bitmap?>): Flow<Bitmap?> {
        return processFilteredBitmap(
            bitmapFlow,
            filtersRepository.filterStatesFlow
        ) { image, filters ->
            val selected = filters.find { it.isSelected }
            if (selected != null && selected.filterType != FilterType.None) {
                filterProcessor.applyFilter(image, selected.filterType)
            } else {
                image
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(filtersRepository: FiltersRepository): ApplyFilterUseCase
    }
}