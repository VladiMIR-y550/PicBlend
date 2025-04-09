package ua.smartmir.picblend.features.remote.domain.model

data class UnsplashUser(
    val id: String,
    val username: String,
    val name: String,
    val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    val totalLikes: Int,
    val totalPhotos: Int,
    val totalCollections: Int,
    val profileImage: UnsplashUrls,
    val links: UnsplashLinks
)
