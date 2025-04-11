package ua.smartmir.picblend.di.filters

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.smartmir.picblend.di.ApplyFilter
import ua.smartmir.picblend.di.Camera
import ua.smartmir.picblend.di.Editor
import ua.smartmir.picblend.features.filters.data.FiltersRepository
import ua.smartmir.picblend.features.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.features.filters.domain.usecase.ChooseFilterUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @Camera
    fun provideCameraApplyFiltersUseCase(
        factory: ApplyFilterUseCase.Factory,
        @Camera filtersRepository: FiltersRepository
    ): ApplyFilterUseCase = factory.create(filtersRepository)

    @Provides
    @Editor
    fun provideEditorApplyFiltersUseCase(
        factory: ApplyFilterUseCase.Factory,
        @Editor filtersRepository: FiltersRepository
    ): ApplyFilterUseCase = factory.create(filtersRepository)

    @Provides
    @ApplyFilter
    fun provideCameraApplyFilterUseCase(
        factory: ApplyFilterUseCase.Factory,
        @Editor filtersRepository: FiltersRepository
    ): ApplyFilterUseCase = factory.create(filtersRepository)

    @Provides
    @Camera
    fun provideCameraChooseFiltersUseCase(
        factory: ChooseFilterUseCase.Factory,
        @Camera filtersRepository: FiltersRepository
    ): ChooseFilterUseCase = factory.create(filtersRepository)

    @Provides
    @Editor
    fun provideEditorChooseFiltersUseCase(
        factory: ChooseFilterUseCase.Factory,
        @Editor filtersRepository: FiltersRepository
    ): ChooseFilterUseCase = factory.create(filtersRepository)
}