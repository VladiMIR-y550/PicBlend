package ua.smartmir.picblend.features.camera.data

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.smartmir.picblend.features.camera.data.model.mapTo
import ua.smartmir.picblend.features.camera.domain.model.CameraSettings
import javax.inject.Inject

interface CameraRepository {
    val camerasFlow: Flow<List<CameraSettings>>
    fun lunch(): CameraController
    fun photo(): Flow<Bitmap?>
    fun updateSelectedCamera(cameraId: String)
    fun switchFrontBackCamera()

    class Base @Inject constructor(
        private val cameraDataSource: CameraDataSource,
    ) : CameraRepository {
        override val camerasFlow: Flow<List<CameraSettings>> =
            cameraDataSource.camerasFlow.map { cameraSettings ->
                cameraSettings.map { it.mapTo() }
            }

        override fun lunch(): CameraController {
            return CameraController(cameraDataSource.launchCamera())
        }

        override fun photo(): Flow<Bitmap?> {
            return cameraDataSource.originalBitmapFlow
        }

        override fun updateSelectedCamera(cameraId: String) {
            cameraDataSource.switchCamera(cameraId)
        }

        override fun switchFrontBackCamera() {
            cameraDataSource.switchFrontBackCamera()
        }
    }
}