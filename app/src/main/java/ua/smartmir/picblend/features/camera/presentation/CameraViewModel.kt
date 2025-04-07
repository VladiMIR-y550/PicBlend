package ua.smartmir.picblend.features.camera.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.smartmir.picblend.common.filters.domain.model.FilterType
import ua.smartmir.picblend.common.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.common.filters.domain.usecase.ChooseFilterUseCase
import ua.smartmir.picblend.di.CameraFilters
import ua.smartmir.picblend.features.camera.data.camera.CameraController
import ua.smartmir.picblend.features.camera.domain.LaunchCameraUseCase
import ua.smartmir.picblend.features.camera.domain.SaveImageUseCase
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @CameraFilters applyFilterUseCase: ApplyFilterUseCase,
    @CameraFilters private val filtersUseCase: ChooseFilterUseCase,
    private val launchCameraUseCase: LaunchCameraUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {

    private val isFiltersShowed = MutableStateFlow<Boolean>(false)
    private val lastImageUri = MutableStateFlow<Uri?>(null)

    val uiState = combine(
        applyFilterUseCase.applySelectedFilter(
            launchCameraUseCase.filteredImageFlow().filterNotNull()
        ),
        filtersUseCase.generateFilterPreviews(launchCameraUseCase.originalBitmapFlow()),
        isFiltersShowed,
        lastImageUri,
    ) { image, filters, isFiltersShowed, lastImageUri ->
        CameraState(
            image = image,
            filterList = filters.map { it.mapToStateEntity() },
            isPhotoFiltersShowing = isFiltersShowed,
            lastImageUri = lastImageUri
        )
    }.flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), CameraState())

    fun launchCamera(): CameraController {
        return launchCameraUseCase.startCamera()
    }

    fun takePhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.image?.let {
                saveImageUseCase.saveImageToGallery(it) { uri ->
                    lastImageUri.update { uri }
                }
            }
        }
    }

    fun showPhotoFilters() {
        isFiltersShowed.update { !it }
    }

    fun changeFilter(filterType: FilterType) {
        filtersUseCase.updateChosenFilter(filterType)
    }
}