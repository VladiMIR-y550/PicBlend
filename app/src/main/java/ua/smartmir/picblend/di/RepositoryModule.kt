package ua.smartmir.picblend.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.features.camera.data.saveimage.ImageRepository
import ua.smartmir.picblend.features.camera.data.camera.CameraRepository
import ua.smartmir.picblend.features.camera.data.filters.FiltersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindImageRepository(imageRepository: ImageRepository.Base): ImageRepository

    @Binds
    @Singleton
    abstract fun bindCameraRepository(cameraRepository: CameraRepository.Base): CameraRepository

    @Binds
    @Singleton
    abstract fun bindFilterRepository(filtersRepository: FiltersRepository.Base): FiltersRepository
}