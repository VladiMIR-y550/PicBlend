package ua.smartmir.picblend.features.home.presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.smartmir.picblend.R
import ua.smartmir.picblend.core.base.BaseViewModel
import ua.smartmir.picblend.core.base.HomeEffect
import ua.smartmir.picblend.core.base.HomeEffect.ShareImage
import ua.smartmir.picblend.core.base.HomeEffect.ShowToast
import ua.smartmir.picblend.core.presentation.StableBitmap
import ua.smartmir.picblend.di.Editor
import ua.smartmir.picblend.features.camera.presentation.model.mapTo
import ua.smartmir.picblend.features.filters.domain.model.FilterType
import ua.smartmir.picblend.features.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.features.filters.domain.usecase.ChooseFilterUseCase
import ua.smartmir.picblend.features.home.domain.PickImageUseCase
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.ErrorImageInfo
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.SuccessImageInfo
import ua.smartmir.picblend.features.saveimage.domain.usecase.SaveImageToCashDirUseCase
import ua.smartmir.picblend.features.saveimage.domain.usecase.SaveImageToGalleryUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Editor private val applyFilterUseCase: ApplyFilterUseCase,
    @Editor private val filtersUseCase: ChooseFilterUseCase,
    private val pickImageUseCase: PickImageUseCase,
    private val saveImageUseCase: SaveImageToCashDirUseCase,
    private val saveImageGalleryUseCase: SaveImageToGalleryUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel<HomeState, HomeEffect>(HomeState()) {

    private val rawBitmap = MutableStateFlow<Bitmap?>(null)
    private val filterList = filtersUseCase.generateFilterPreviews(rawBitmap)
        .map { it.map { filter -> filter.mapTo() } }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            combine(
                applyFilterUseCase.applySelectedFilter(rawBitmap),
                filterList
            ) { image, filters ->
                image?.let {
                    _uiState.update { state ->
                        state.copy(
                            image = StableBitmap(image),
                            filterList = filters
                        )
                    }
                }
            }.collect()
        }
    }

    fun updateIsPermissionNeeded(isNeed: Boolean) {
        _uiState.update { state ->
            state.copy(isPermissionNeeded = isNeed)
        }
    }

    fun loadImageFromGallery(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            pickImageUseCase.imageByUri(uri)?.let { selectedBitmap ->
                rawBitmap.update { selectedBitmap }
            }
        }
    }

    fun showPhotoFilters() {
        _uiState.update { state ->
            state.copy(isPhotoFiltersShowing = !state.isPhotoFiltersShowing)
        }
    }

    fun changeFilter(filterType: FilterType) {
        filtersUseCase.updateChosenFilter(filterType)
    }

    fun shareImage() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.image?.let {
                saveImageUseCase.saveImage(it.bitmap) { result ->
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
                saveImageGalleryUseCase.saveImage(it.bitmap) { result ->
                    when (result) {
                        is SuccessImageInfo -> sendEffect(
                            ShowToast(
                                context.getString(R.string.saved, result.name)
                            )
                        )

                        is ErrorImageInfo -> sendEffect(ShowToast(result.errorMessage))
                    }
                }
            }
        }
    }

    fun showExitAppDialog(showDialog: Boolean = true) {
        _uiState.update { state ->
            state.copy(
                isExitDialogShowed = showDialog
            )

        }
    }
}