package ua.smartmir.picblend.di.gallery

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.features.home.data.GalleryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GalleryRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGalleryRepository(galleryRepository: GalleryRepository.Base): GalleryRepository
}