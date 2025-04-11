package ua.smartmir.picblend.features.camera.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.smartmir.picblend.core.base.BaseViewModel
import ua.smartmir.picblend.core.base.CameraEffect
import ua.smartmir.picblend.core.base.CameraEffect.ShowToast
import ua.smartmir.picblend.core.presentation.StableBitmap
import ua.smartmir.picblend.di.Camera
import ua.smartmir.picblend.features.camera.data.CameraController
import ua.smartmir.picblend.features.camera.domain.LaunchCameraUseCase
import ua.smartmir.picblend.features.camera.presentation.model.CameraSettingsUi
import ua.smartmir.picblend.features.camera.presentation.model.mapTo
import ua.smartmir.picblend.features.filters.domain.model.FilterType
import ua.smartmir.picblend.features.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.features.filters.domain.usecase.ChooseFilterUseCase
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.ErrorImageInfo
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.SuccessImageInfo
import ua.smartmir.picblend.features.saveimage.domain.usecase.SaveImageToGalleryUseCase
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @Camera applyFilterUseCase: ApplyFilterUseCase,
    @Camera private val filtersUseCase: ChooseFilterUseCase,
    private val launchCameraUseCase: LaunchCameraUseCase,
    private val saveImageUseCase: SaveImageToGalleryUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel<CameraState, CameraEffect>(CameraState()) {

    private val filteredImage = applyFilterUseCase.applySelectedFilter(
        launchCameraUseCase.filteredImageFlow().filterNotNull()
    )

    private val filterList =
        filtersUseCase.generateFilterPreviews(launchCameraUseCase.originalBitmapFlow())
            .map { previews -> previews.map { it.mapTo() } }

    private val availableCameras = launchCameraUseCase.availableCameras()
        .map { it.map { camera -> camera.mapTo(context) } }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            combine(
                filteredImage,
                filterList,
                availableCameras
            ) { image, filterList, cameras ->
                if (uiState.value.image != image) {
                    image?.let {
                        _uiState.update {
                            it.copy(image = StableBitmap(bitmap = image))
                        }
                    }
                }

                if (uiState.value.filterList != filterList) {
                    _uiState.update {
                        it.copy(filterList = filterList)
                    }
                }

                if (uiState.value.cameras != cameras) {
                    _uiState.update {
                        it.copy(cameras = cameras)
                    }
                }
            }.collect()
        }
    }

    fun launchCamera(): CameraController {
        return launchCameraUseCase.startCamera()
    }

    fun takePhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.image?.let {
                saveImageUseCase.saveImage(it.bitmap) { result ->
                    when (result) {
                        is SuccessImageInfo -> updateLastUri(result.uri)
                        is ErrorImageInfo -> sendEffect(ShowToast(result.errorMessage))
                    }
                }
            }
        }
    }

    fun updateLastUri(uri: Uri) {
        _uiState.update { state ->
            state.copy(lastImageUri = uri)
        }
    }

    fun switchFrontBackCamera() {
        launchCameraUseCase.switchFrontBackCamera()
    }

    fun changeCamera(cameraSettings: CameraSettingsUi) {
        launchCameraUseCase.updateChosenCamera(cameraSettings.cameraId)
    }

    fun showPhotoFilters() {
        _uiState.update {
            it.copy(isPhotoFiltersShowing = !it.isPhotoFiltersShowing)
        }
    }

    fun changeFilter(filterType: FilterType) {
        filtersUseCase.updateChosenFilter(filterType)
    }
}