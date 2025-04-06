package ua.smartmir.picblend.features.camera.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import ua.smartmir.picblend.features.camera.data.camera.CameraRepository
import ua.smartmir.picblend.features.camera.domain.FilterType
import ua.smartmir.picblend.features.camera.data.filters.FiltersRepository
import javax.inject.Inject

class FiltersUseCase @Inject constructor(
    private val filtersRepository: FiltersRepository,
    private val cameraRepository: CameraRepository,
    private val filterProcessor: FilterProcessor
) {
    companion object {
        private const val RESIZE_VALUE = 150
    }

    fun updateChosenFilter(filterType: FilterType) =
        filtersRepository.updateSelectedFilter(filterType)

    @OptIn(FlowPreview::class)
    fun filters(): Flow<List<FilterDomainEntity>> {
        return combine(
            cameraRepository.imageDate(),
            filtersRepository.filterStatesFlow,
        ) { image, filters ->
            withContext(Dispatchers.Default) {
                image?.let {
                    val resized = filterProcessor.resizeWithAspectRatio(image, RESIZE_VALUE)
                    filters.map { state ->
                        val filtered = filterProcessor.applyFilter(resized, state.filterType)
                        state.copy(filteredImageData = filtered)
                    }
                } ?: filters
            }
        }
    }
}