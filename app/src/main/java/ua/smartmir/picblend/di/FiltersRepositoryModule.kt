package ua.smartmir.picblend.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ua.smartmir.picblend.common.filters.domain.repository.FiltersRepository
import ua.smartmir.picblend.common.filters.data.FiltersRepositoryBase

@Module
@InstallIn(ViewModelComponent::class)
object FiltersRepositoryModule {

    @Provides
    @ViewModelScoped
    @Camera
    fun provideCameraFiltersRepository(): FiltersRepository = FiltersRepositoryBase()

    @Provides
    @ViewModelScoped
    @Editor
    fun provideEditorFiltersRepository(): FiltersRepository = FiltersRepositoryBase()
}