package ua.smartmir.picblend.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.features.camera.data.camera.CameraDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraModule {

    @Binds
    abstract fun bindCameraDataSource(cameraDataSource: CameraDataSource.CameraXDataSource): CameraDataSource
}