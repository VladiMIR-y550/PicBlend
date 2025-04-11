package ua.smartmir.picblend.di.remote_images

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.features.remote_images.data.repository.ImagesDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindUnsplashDataSource(unsplashDataSource: ImagesDataSource.UnsplashImagesSource): ImagesDataSource
}