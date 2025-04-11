package ua.smartmir.picblend.features.remote_images.presentation.model

import androidx.compose.runtime.Stable
import ua.smartmir.picblend.features.remote_images.domain.model.UnsplashLinks

@Stable
data class LinksUi(
    val self: String,
    val html: String,
    val photos: String?,
    val likes: String?,
    val portfolio: String?,
    val download: String?,
    val downloadLocation: String?
)

fun UnsplashLinks.mapTo(): LinksUi = LinksUi(
    self = self,
    html = html,
    photos = photos,
    likes = likes,
    portfolio = portfolio,
    download = download,
    downloadLocation = downloadLocation
)
