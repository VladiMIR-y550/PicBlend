package ua.smartmir.picblend.features.home.presentation

import android.graphics.Bitmap
import android.net.Uri
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
import ua.smartmir.picblend.core.base.BaseViewModel
import ua.smartmir.picblend.core.base.HomeEffect
import ua.smartmir.picblend.core.base.HomeEffect.ShareImage
import ua.smartmir.picblend.core.base.HomeEffect.ShowToast
import ua.smartmir.picblend.features.filters.domain.model.FilterType
import ua.smartmir.picblend.features.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.features.filters.domain.usecase.ChooseFilterUseCase
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.ErrorImageInfo
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.SuccessImageInfo
import ua.smartmir.picblend.features.saveimage.domain.usecase.SaveImageToCashDirUseCase
import ua.smartmir.picblend.features.saveimage.domain.usecase.SaveImageToGalleryUseCase
import ua.smartmir.picblend.di.Editor
import ua.smartmir.picblend.features.camera.presentation.model.mapToStateEntity
import ua.smartmir.picblend.features.home.domain.PickImageUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Editor applyFilterUseCase: ApplyFilterUseCase,
    @Editor private val filtersUseCase: ChooseFilterUseCase,
    private val pickImageUseCase: PickImageUseCase,
    private val saveImageUseCase: SaveImageToCashDirUseCase,
    private val saveImageGalleryUseCase: SaveImageToGalleryUseCase
) : BaseViewModel<HomeEffect>() {

    private val image = MutableStateFlow<Bitmap?>(null)
    private val uriCashedImage = MutableStateFlow<Uri?>(null)
    private val isPermissionNeeded = MutableStateFlow<Boolean>(false)
    private val isFiltersShowed = MutableStateFlow<Boolean>(false)

    val uiState = combine(
        image.filterNotNull(),
        uriCashedImage,
        filtersUseCase.generateFilterPreviews(image),
        isFiltersShowed,
        isPermissionNeeded,
    ) { image, uri, filters, isFiltersShowed, isPermissionNeeded ->
        HomeState(
            image = applyFilterUseCase.applySelectedFilter(image),
            uriCashedImage = uri,
            filterList = filters.map { it.mapToStateEntity() },
            isPermissionNeeded = isPermissionNeeded,
            isPhotoFiltersShowing = isFiltersShowed
        )
    }.flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeState())

    fun launchRequestPermission() {
        isPermissionNeeded.update { true }
    }

    fun resetPermissionNeededState() {
        isPermissionNeeded.update { false }
    }

    fun loadImageFromGallery(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            pickImageUseCase.imageByUri(uri)?.let { selectedBitmap ->
                image.update { selectedBitmap }
            }
        }
    }

    fun showPhotoFilters() {
        isFiltersShowed.update { !it }
    }

    fun changeFilter(filterType: FilterType) {
        filtersUseCase.updateChosenFilter(filterType)
    }

    fun shareImage() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.image?.let {
                saveImageUseCase.saveImage(it) { result ->
                    when (result) {
                        is SuccessImageInfo -> sendEffect(ShareImage(result.uri))
                        is ErrorImageInfo -> sendEffect(ShowToast(result.errorMessage))
                    }
                }
            }
        }
    }

    fun saveImage() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.image?.let {
                saveImageGalleryUseCase.saveImage(it) { result ->
                    when (result) {
                        is SuccessImageInfo -> sendEffect(ShowToast("Saved ${result.name}"))
                        is ErrorImageInfo -> sendEffect(ShowToast(result.errorMessage))
                    }
                }
            }
        }
    }
}