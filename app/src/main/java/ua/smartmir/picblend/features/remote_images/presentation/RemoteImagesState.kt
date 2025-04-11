package ua.smartmir.picblend.features.remote_images.presentation

import ua.smartmir.picblend.core.base.ScreenState
import ua.smartmir.picblend.features.remote_images.presentation.model.PhotoUi

data class RemoteImagesState(
    val images: List<PhotoUi> = emptyList()
) : ScreenState
