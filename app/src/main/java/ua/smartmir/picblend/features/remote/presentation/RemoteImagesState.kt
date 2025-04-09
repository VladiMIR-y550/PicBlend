package ua.smartmir.picblend.features.remote.presentation

import ua.smartmir.picblend.base.ScreenState
import ua.smartmir.picblend.features.remote.presentation.model.PhotoUi

data class RemoteImagesState(
    val images: List<PhotoUi> = emptyList()
) : ScreenState
