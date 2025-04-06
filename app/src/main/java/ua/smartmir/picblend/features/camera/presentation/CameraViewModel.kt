package ua.smartmir.picblend.features.camera.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.smartmir.picblend.features.camera.data.camera.CameraController
import ua.smartmir.picblend.features.camera.domain.FilterType
import ua.smartmir.picblend.features.camera.domain.FiltersUseCase
import ua.smartmir.picblend.features.camera.domain.LaunchCameraUseCase
import ua.smartmir.picblend.features.camera.domain.SaveImageUseCase
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val filtersUseCase: FiltersUseCase,
    private val launchCameraUseCase: LaunchCameraUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {

    private val isFiltersShowed = MutableStateFlow<Boolean>(false)
    private val lastImageUri = MutableStateFlow<Uri?>(null)

    val uiState = combine(
        launchCameraUseCase.originalBitmapFlow().filterNotNull(),
        isFiltersShowed,
        lastImageUri,
        filtersUseCase.filters()
    ) { imageData, isFiltersShowed, lastImageUri, filters ->
        CameraState(
            mainPreviewBitmap = imageData,
            filterList = filters.map {
                it.mapToStateEntity()
            },
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
            uiState.value.mainPreviewBitmap?.let {
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