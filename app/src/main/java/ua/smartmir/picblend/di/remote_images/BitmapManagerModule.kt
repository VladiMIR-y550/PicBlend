package ua.smartmir.picblend.di.remote_images

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.features.remote_images.domain.BitmapManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BitmapManagerModule {

    @Binds
    @Singleton
    abstract fun bindBitmapManager(bitmapManager: BitmapManager.CoilBitmapManager): BitmapManager
}