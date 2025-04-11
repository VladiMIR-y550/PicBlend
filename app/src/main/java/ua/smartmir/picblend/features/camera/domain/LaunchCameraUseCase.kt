package ua.smartmir.picblend.features.camera.domain

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import ua.smartmir.picblend.features.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.di.ApplyFilter
import ua.smartmir.picblend.features.camera.data.CameraController
import ua.smartmir.picblend.features.camera.data.CameraRepository
import ua.smartmir.picblend.features.camera.domain.model.CameraSettings
import javax.inject.Inject

class LaunchCameraUseCase @Inject constructor(
    @ApplyFilter private val filterUseCase: ApplyFilterUseCase,
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

    fun availableCameras(): Flow<List<CameraSettings>> {
        return cameraRepository.camerasFlow
    }

    fun updateChosenCamera(cameraId: String) =
        cameraRepository.updateSelectedCamera(cameraId)

    fun switchFrontBackCamera() = cameraRepository.switchFrontBackCamera()
}