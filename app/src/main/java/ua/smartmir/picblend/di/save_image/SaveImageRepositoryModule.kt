package ua.smartmir.picblend.di.save_image

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.di.Camera
import ua.smartmir.picblend.di.Editor
import ua.smartmir.picblend.features.saveimage.data.repository.CacheDirStorage
import ua.smartmir.picblend.features.saveimage.data.repository.GalleryStorage
import ua.smartmir.picblend.features.saveimage.domain.repository.ImageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SaveImageRepositoryModule {
    @Binds
    @Camera
    @Singleton
    abstract fun bindCameraPhotoImageRepository(imageRepository: GalleryStorage): ImageRepository

    @Binds
    @Editor
    @Singleton
    abstract fun bindFinalImageRepository(imageRepository: CacheDirStorage): ImageRepository
}