package ua.smartmir.picblend.di.camera

import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ua.smartmir.picblend.features.camera.data.CameraDataSource
import ua.smartmir.picblend.features.camera.data.CameraRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class CameraDataModule {

    @ExperimentalCamera2Interop
    @Binds
    @ViewModelScoped
    abstract fun bindCameraDataSource(cameraDataSource: CameraDataSource.CameraXDataSource): CameraDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindCameraRepository(cameraRepository: CameraRepository.Base): CameraRepository
}