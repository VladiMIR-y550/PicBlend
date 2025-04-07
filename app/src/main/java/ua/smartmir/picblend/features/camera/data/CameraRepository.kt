package ua.smartmir.picblend.features.camera.data

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CameraRepository {
    fun lunch(): CameraController
    fun photo(): Flow<Bitmap?>

    class Base @Inject constructor(
        private val cameraDataSource: CameraDataSource,
    ) : CameraRepository {

        override fun lunch(): CameraController {
            return CameraController(cameraDataSource.launchCamera())
        }

        override fun photo(): Flow<Bitmap?> {
            return cameraDataSource.originalBitmapFlow
        }
    }
}
