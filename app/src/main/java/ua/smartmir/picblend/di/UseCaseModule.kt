package ua.smartmir.picblend.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.smartmir.picblend.common.filters.data.FiltersRepository
import ua.smartmir.picblend.common.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.common.filters.domain.usecase.ChooseFilterUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @CameraFilters
    fun provideCameraApplyFiltersUseCase(
        factory: ApplyFilterUseCase.Factory,
        @CameraFilters filtersRepository: FiltersRepository
    ): ApplyFilterUseCase = factory.create(filtersRepository)

    @Provides
    @EditorFilters
    fun provideEditorApplyFiltersUseCase(
        factory: ApplyFilterUseCase.Factory,
        @EditorFilters filtersRepository: FiltersRepository
    ): ApplyFilterUseCase = factory.create(filtersRepository)

    @Provides
    @CameraApplyFilter
    fun provideCameraApplyFilterUseCase(
        factory: ApplyFilterUseCase.Factory,
        @EditorFilters filtersRepository: FiltersRepository
    ): ApplyFilterUseCase = factory.create(filtersRepository)

    @Provides
    @CameraFilters
    fun provideCameraChooseFiltersUseCase(
        factory: ChooseFilterUseCase.Factory,
        @CameraFilters filtersRepository: FiltersRepository
    ): ChooseFilterUseCase = factory.create(filtersRepository)

    @Provides
    @EditorFilters
    fun provideEditorChooseFiltersUseCase(
        factory: ChooseFilterUseCase.Factory,
        @EditorFilters filtersRepository: FiltersRepository
    ): ChooseFilterUseCase = factory.create(filtersRepository)
}