package ua.smartmir.picblend.features.remote_images.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.smartmir.picblend.core.ConnectionService
import ua.smartmir.picblend.core.NetworkStatus
import ua.smartmir.picblend.core.base.BaseViewModel
import ua.smartmir.picblend.core.base.RemoteImagesEffect
import ua.smartmir.picblend.core.base.RemoteImagesEffect.CachedImage
import ua.smartmir.picblend.core.base.RemoteImagesEffect.Loading
import ua.smartmir.picblend.core.base.RemoteImagesEffect.ShowToast
import ua.smartmir.picblend.core.isNetworkConnected
import ua.smartmir.picblend.features.remote_images.domain.usecase.GetBitmapUseCase
import ua.smartmir.picblend.features.remote_images.domain.usecase.LoadAllImagesUseCase
import ua.smartmir.picblend.features.remote_images.presentation.model.PhotoUi
import ua.smartmir.picblend.features.remote_images.presentation.model.mapTo
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.ErrorImageInfo
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.SuccessImageInfo
import ua.smartmir.picblend.features.saveimage.domain.usecase.SaveImageToCashDirUseCase
import javax.inject.Inject

@HiltViewModel
class RemoteImagesViewModel @Inject constructor(
    private val imagesUseCase: LoadAllImagesUseCase,
    private val bitmapUseCase: GetBitmapUseCase,
    private val saveImageToCashDirUseCase: SaveImageToCashDirUseCase,
    private val connectionService: ConnectionService
) : BaseViewModel<RemoteImagesState, RemoteImagesEffect>(RemoteImagesState()) {

    companion object {
        const val LOAD_MORE_THRESHOLD_INDEX = 5
        const val PLACEHOLDER_LIST_SIZE = 8
    }

    val networkDialogIsVisible = connectionService.networkStatus
        .map { status ->
            status == NetworkStatus.Disconnected
        }.onEach { isDisconnected ->
            if (!isDisconnected) getMoreImages()
        }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(),
            false
        )

    fun getMoreImages() {
        if (connectionService.isNetworkConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                sendEffect(Loading(true))
                imagesUseCase.loadImages().fold(
                    onSuccess = { images ->
                        val newImages = images.map { it.mapTo() }
                        _uiState.update { state ->
                            state.copy(
                                images = state.images.addAllUniqueItems(newImages)
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

fun List<PhotoUi>.addAllUniqueItems(from: List<PhotoUi>): List<PhotoUi> {
    val oldList = this.toMutableList()
    from.forEach { item ->
        if (oldList.all { it.id != item.id }) {
            oldList.add(item)
        }
    }
    return oldList.toList()
}