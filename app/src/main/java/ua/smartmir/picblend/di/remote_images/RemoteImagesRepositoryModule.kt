package ua.smartmir.picblend.di.remote_images

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.features.remote_images.data.repository.RemoteImagesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteImagesRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRemoteImagesRepository(remoteImagesRepository: RemoteImagesRepository.UnsplashImagesRepository): RemoteImagesRepository

}