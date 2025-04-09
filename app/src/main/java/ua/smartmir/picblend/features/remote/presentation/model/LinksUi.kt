package ua.smartmir.picblend.features.remote.presentation.model

import ua.smartmir.picblend.features.remote.domain.model.UnsplashLinks

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
