package ua.smartmir.picblend.features.camera.domain

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import ua.smartmir.picblend.features.camera.data.camera.CameraController
import ua.smartmir.picblend.features.camera.data.camera.CameraRepository
import ua.smartmir.picblend.features.camera.domain.FilterType
import ua.smartmir.picblend.features.camera.data.filters.FiltersRepository
import javax.inject.Inject

class LaunchCameraUseCase @Inject constructor(
    private val cameraRepository: CameraRepository,
    private val filtersRepository: FiltersRepository,
    private val filterProcessor: FilterProcessor

) {

    fun originalBitmapFlow(): Flow<Bitmap?> {
        return combine(
            cameraRepository.imageDate().filterNotNull(),
            filtersRepository.filterStatesFlow
        ) { image, filters ->
            withContext(Dispatchers.Default) {
                val selectedFilter = filters.find { it.isSelected }
                if (selectedFilter != null && selectedFilter.filterType != FilterType.None) {
                    filterProcessor.applyFilter(image, selectedFilter.filterType)
                } else {
                    image
                }
            }
        }
    }

    fun startCamera(): CameraController {
        return cameraRepository.lunch()
    }
}