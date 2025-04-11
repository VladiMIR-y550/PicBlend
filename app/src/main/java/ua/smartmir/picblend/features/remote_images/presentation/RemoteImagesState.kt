package ua.smartmir.picblend.features.remote_images.presentation

import androidx.compose.runtime.Immutable
import ua.smartmir.picblend.core.base.ScreenState
import ua.smartmir.picblend.features.remote_images.presentation.model.PhotoUi

@Immutable
data class RemoteImagesState(
    val images: List<PhotoUi> = emptyList()
) : ScreenState
