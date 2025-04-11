package ua.smartmir.picblend.features.remote_images.presentation.model

import ua.smartmir.picblend.features.remote_images.domain.model.UnsplashUrls

data class UrlsUi(
    val thumb: String,
    val small: String,
    val medium: String,
    val regular: String,
    val large: String,
    val full: String,
    val raw: String
)

fun UnsplashUrls.mapTo(): UrlsUi = UrlsUi(
    thumb = thumb,
    small = small,
    medium = medium,
    regular = regular,
    large = large,
    full = full,
    raw = raw
)


