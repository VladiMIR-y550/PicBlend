package ua.smartmir.picblend.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.common.filters.utils.FilterProcessor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindFilterProcessor(filterProcessor: FilterProcessor.PhotoFilterProcessor): FilterProcessor
}