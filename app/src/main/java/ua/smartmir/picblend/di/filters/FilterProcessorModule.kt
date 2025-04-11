package ua.smartmir.picblend.di.filters

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.features.filters.utils.ImageProcessor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FilterProcessorModule {

    @Binds
    @Singleton
    abstract fun bindFilterProcessor(filterProcessor: ImageProcessor.PhotoFilterProcessor): ImageProcessor
}