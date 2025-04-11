package ua.smartmir.picblend.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.common.saveimage.data.repository.GalleryStorage
import ua.smartmir.picblend.common.saveimage.data.repository.CacheDirStorage
import ua.smartmir.picblend.common.saveimage.domain.repository.ImageRepository
import ua.smartmir.picblend.features.camera.data.CameraRepository
import ua.smartmir.picblend.features.home.data.GalleryRepository
import ua.smartmir.picblend.features.remote.data.repository.RemoteImagesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Camera
    @Singleton
    abstract fun bindCameraPhotoImageRepository(imageRepository: GalleryStorage): ImageRepository

    @Binds
    @Editor
    @Singleton
    abstract fun bindFinalImageRepository(imageRepository: CacheDirStorage): ImageRepository

    @Binds
    @Singleton
    abstract fun bindCameraRepository(cameraRepository: CameraRepository.Base): CameraRepository

    @Binds
    @Singleton
    abstract fun bindGalleryRepository(galleryRepository: GalleryRepository.Base): GalleryRepository

    @Binds
    @Singleton
    abstract fun bindRemoteImagesRepository(remoteImagesRepository: RemoteImagesRepository.UnsplashImagesRepository): RemoteImagesRepository
}