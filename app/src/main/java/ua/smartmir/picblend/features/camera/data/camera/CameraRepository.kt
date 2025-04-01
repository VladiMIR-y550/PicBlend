package ua.smartmir.picblend.features.camera.data.camera

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

interface CameraRepository {
    fun lunch(): CameraController
    fun imageDate(): Flow<Bitmap?>

    class Base @Inject constructor(
        private val cameraDataSource: CameraDataSource,
    ) : CameraRepository {

        override fun lunch(): CameraController {
            return CameraController(cameraDataSource.launchCamera())
        }

        override fun imageDate(): Flow<Bitmap?> {
            return cameraDataSource.originalBitmapFlow
        }
    }
}
