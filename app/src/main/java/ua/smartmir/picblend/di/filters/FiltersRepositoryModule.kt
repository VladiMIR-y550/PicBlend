package ua.smartmir.picblend.di.filters

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ua.smartmir.picblend.di.Camera
import ua.smartmir.picblend.di.Editor
import ua.smartmir.picblend.features.filters.data.FiltersRepository

@Module
@InstallIn(ViewModelComponent::class)
object FiltersRepositoryModule {

    @Provides
    @ViewModelScoped
    @Camera
    fun provideCameraFiltersRepository(): FiltersRepository = FiltersRepository.Base()

    @Provides
    @ViewModelScoped
    @Editor
    fun provideEditorFiltersRepository(): FiltersRepository = FiltersRepository.Base()
}