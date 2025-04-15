package ua.smartmir.picblend.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.di.camera.CameraModule
import ua.smartmir.picblend.di.filters.FilterProcessorModule
import ua.smartmir.picblend.di.gallery.GalleryRepositoryModule
import ua.smartmir.picblend.di.remote_images.network.ApiModule

@Module(
    includes = [
        GalleryRepositoryModule::class,
        FilterProcessorModule::class,
        ApiModule::class,
        CameraModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
open class HiltModule