package ua.smartmir.picblend.features.filters.domain.usecase

import android.graphics.Bitmap
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import ua.smartmir.picblend.features.filters.data.FiltersRepository
import ua.smartmir.picblend.features.filters.domain.model.Filter
import ua.smartmir.picblend.features.filters.domain.model.FilterType
import ua.smartmir.picblend.features.filters.utils.ImageProcessor
import ua.smartmir.picblend.features.filters.utils.processFilteredBitmap

class ChooseFilterUseCase @AssistedInject constructor(
    @Assisted private val filtersRepository: FiltersRepository,
    private val filterProcessor: ImageProcessor
) {
    companion object {
        private const val RESIZE_VALUE = 250
    }

    fun updateChosenFilter(filterType: FilterType) =
        filtersRepository.updateSelectedFilter(filterType)

    fun generateFilterPreviews(bitmapFlow: Flow<Bitmap?>): Flow<List<Filter>> {
        return processFilteredBitmap(
            bitmapFlow,
            filtersRepository.filterStatesFlow
        ) { image, filters ->
            val resized = filterProcessor.resizeWithAspectRatio(image, RESIZE_VALUE)
            filters.map { filterState ->
                val filtered = filterProcessor.applyFilter(resized, filterState.filterType)
                filterState.copy(filteredImageData = filtered)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(filtersRepository: FiltersRepository): ChooseFilterUseCase
    }
}