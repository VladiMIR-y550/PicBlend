package ua.smartmir.picblend.features.remote.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.smartmir.picblend.base.BaseViewModel
import ua.smartmir.picblend.base.RemoteImagesEffect
import ua.smartmir.picblend.base.RemoteImagesEffect.CachedImage
import ua.smartmir.picblend.base.RemoteImagesEffect.Loading
import ua.smartmir.picblend.base.RemoteImagesEffect.ShowToast
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult.ErrorImageInfo
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult.SuccessImageInfo
import ua.smartmir.picblend.common.saveimage.domain.usecase.SaveImageToCashDirUseCase
import ua.smartmir.picblend.features.remote.domain.usecase.GetBitmapUseCase
import ua.smartmir.picblend.features.remote.domain.usecase.LoadAllImagesUseCase
import ua.smartmir.picblend.features.remote.presentation.model.PhotoUi
import ua.smartmir.picblend.features.remote.presentation.model.mapTo
import javax.inject.Inject

@HiltViewModel
class RemoteImagesViewModel @Inject constructor(
    private val imagesUseCase: LoadAllImagesUseCase,
    private val bitmapUseCase: GetBitmapUseCase,
    private val saveImageToCashDirUseCase: SaveImageToCashDirUseCase
) : BaseViewModel<RemoteImagesEffect>() {
    private var currentPage = 0
    private var _uiState = MutableStateFlow<RemoteImagesState>(RemoteImagesState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getMoreImages()
    }

    fun getMoreImages() {
        currentPage++
        viewModelScope.launch(Dispatchers.IO) {
            sendEffect(Loading(true))
            imagesUseCase.loadImages(currentPage).fold(
                onSuccess = { images ->
                    val newImages = images.map { it.mapTo() }
                    _uiState.update { state ->
                        state.copy(
                            images = state.images + newImages
                        )
                    }
                    sendEffect(Loading(false))
                },
                onFailure = {
                    sendEffect(Loading(false))
                    sendEffect(ShowToast(it.message.toString()))
                }
            )
        }
    }

    fun cacheImage(image: PhotoUi) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageUrl = image.urls.regular
            bitmapUseCase.getBitmap(imageUrl)?.let { bitmap ->
                imagesUseCase.trackDownload(imageUrl)
                saveImageToCashDirUseCase.saveImage(bitmap) { result ->
                    when (result) {
                        is SuccessImageInfo -> sendEffect(CachedImage(result.uri))
                        is ErrorImageInfo -> sendEffect(ShowToast(result.errorMessage))
                    }
                }
            }
        }
    }
}