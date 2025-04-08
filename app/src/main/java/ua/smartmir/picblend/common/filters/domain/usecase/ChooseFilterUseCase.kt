package ua.smartmir.picblend.common.filters.domain.usecase

import android.graphics.Bitmap
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import ua.smartmir.picblend.common.filters.domain.repository.FiltersRepository
import ua.smartmir.picblend.common.filters.domain.model.FilterDomainEntity
import ua.smartmir.picblend.common.filters.domain.model.FilterType
import ua.smartmir.picblend.common.filters.utils.FilterProcessor
import ua.smartmir.picblend.common.filters.utils.processFilteredBitmap
import ua.smartmir.picblend.common.filters.utils.processFilteredBitmapSingle

class ChooseFilterUseCase @AssistedInject constructor(
    @Assisted private val filtersRepository: FiltersRepository,
    private val filterProcessor: FilterProcessor
) {
    companion object {
        private const val RESIZE_VALUE = 250
    }

    fun updateChosenFilter(filterType: FilterType) =
        filtersRepository.updateSelectedFilter(filterType)

    suspend fun generateFilterPreviews(
        bitmap: Bitmap
    ): List<FilterDomainEntity> {
        return processFilteredBitmapSingle(
            bitmap,
            filtersRepository.filterStatesFlow.value
        ) { image, filtersList ->
            val resized = filterProcessor.resizeWithAspectRatio(image, RESIZE_VALUE)
            filtersList.map { filterState ->
                val filtered = filterProcessor.applyFilter(resized, filterState.filterType)
                filterState.copy(filteredImageData = filtered)
            }
        }
    }

    fun generateFilterPreviews(bitmapFlow: Flow<Bitmap?>): Flow<List<FilterDomainEntity>> {
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