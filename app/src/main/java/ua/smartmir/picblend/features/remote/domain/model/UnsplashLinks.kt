package ua.smartmir.picblend.features.remote.domain.model

data class UnsplashLinks(
    val self: String,
    val html: String,
    val photos: String?,
    val likes: String?,
    val portfolio: String?,
    val download: String?,
    val downloadLocation: String?
)
