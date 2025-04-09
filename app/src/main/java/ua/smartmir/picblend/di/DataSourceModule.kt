package ua.smartmir.picblend.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.features.camera.data.CameraDataSource
import ua.smartmir.picblend.features.remote.data.repository.ImagesDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindCameraDataSource(cameraDataSource: CameraDataSource.CameraXDataSource): CameraDataSource

    @Binds
    @Singleton
    abstract fun bindUnsplashDataSource(unsplashDataSource: ImagesDataSource.UnsplashImagesSource): ImagesDataSource
}