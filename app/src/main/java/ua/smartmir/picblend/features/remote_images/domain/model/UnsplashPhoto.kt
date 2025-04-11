package ua.smartmir.picblend.features.remote_images.domain.model

data class UnsplashPhoto(
    val id: String,
    val createdAt: String,
    val width: Int,
    val height: Int,
    val color: String?,
    val likes: Int,
    val description: String?,
    val urls: UnsplashUrls,
    val links: UnsplashLinks,
    val user: UnsplashUser
)
