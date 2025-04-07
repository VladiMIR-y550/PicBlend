package ua.smartmir.picblend.features.camera.domain

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import ua.smartmir.picblend.common.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.di.CameraApplyFilter
import ua.smartmir.picblend.features.camera.data.camera.CameraController
import ua.smartmir.picblend.features.camera.data.camera.CameraRepository
import javax.inject.Inject

class LaunchCameraUseCase @Inject constructor(
    @CameraApplyFilter private val filterUseCase: ApplyFilterUseCase,
    private val cameraRepository: CameraRepository
) {
    fun originalBitmapFlow(): Flow<Bitmap?> {
        return cameraRepository.photo()
    }

    fun filteredImageFlow(): Flow<Bitmap?> {
        return filterUseCase.applySelectedFilter(cameraRepository.photo())
    }

    fun startCamera(): CameraController {
        return cameraRepository.lunch()
    }
}