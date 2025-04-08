package ua.smartmir.picblend.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.common.saveimage.data.repository.CameraPhotoStorage
import ua.smartmir.picblend.common.saveimage.data.repository.FinalImageStore
import ua.smartmir.picblend.common.saveimage.domain.repository.ImageRepository
import ua.smartmir.picblend.features.camera.data.CameraRepository
import ua.smartmir.picblend.features.home.data.GalleryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Camera
    @Singleton
    abstract fun bindCameraPhotoImageRepository(imageRepository: CameraPhotoStorage): ImageRepository

    @Binds
    @Editor
    @Singleton
    abstract fun bindFinalImageRepository(imageRepository: FinalImageStore): ImageRepository

    @Binds
    @Singleton
    abstract fun bindCameraRepository(cameraRepository: CameraRepository.Base): CameraRepository

    @Binds
    @Singleton
    abstract fun bindGalleryRepository(galleryRepository: GalleryRepository.Base): GalleryRepository
}