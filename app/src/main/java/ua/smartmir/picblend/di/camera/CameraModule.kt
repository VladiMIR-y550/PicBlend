package ua.smartmir.picblend.di.camera

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CameraModule {

    @Provides
    fun provideCameraController(@ApplicationContext context: Context): LifecycleCameraController {
        return LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS)
        }
    }

    @Provides
    fun provideCameraProvider(@ApplicationContext context: Context): ProcessCameraProvider {
        return ProcessCameraProvider.getInstance(context).get()
    }

    @Provides
    fun provideCameraManager(@ApplicationContext context: Context): CameraManager {
        return context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}