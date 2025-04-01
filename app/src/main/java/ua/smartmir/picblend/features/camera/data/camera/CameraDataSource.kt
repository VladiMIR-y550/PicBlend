package ua.smartmir.picblend.features.camera.data.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.view.LifecycleCameraController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import ua.smartmir.picblend.di.ApplicationScope
import java.util.concurrent.Executor
import javax.inject.Inject

interface CameraDataSource {
    val originalBitmapFlow: Flow<Bitmap?>
    fun launchCamera(): LifecycleCameraController

    class CameraXDataSource @Inject constructor(
        private val executor: Executor,
        private val cameraController: LifecycleCameraController,
        @ApplicationScope private val appScope: CoroutineScope
    ) : CameraDataSource {
        private val _originalBitmapFlow: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
        override val originalBitmapFlow: Flow<Bitmap?> = _originalBitmapFlow.shareIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1
        )

        override fun launchCamera(): LifecycleCameraController {
            cameraController.clearImageAnalysisAnalyzer()
            cameraController.setImageAnalysisAnalyzer(executor) { image ->
                _originalBitmapFlow.update {
                    image.toBitmap().copy(Bitmap.Config.ARGB_8888, true)
                        .rotateBitmap(image.imageInfo.rotationDegrees)
                }
                image.close()
            }
            return cameraController
        }
    }
}

private fun Bitmap.rotateBitmap(degrees: Int): Bitmap {
    if (degrees == 0) return this
    val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}