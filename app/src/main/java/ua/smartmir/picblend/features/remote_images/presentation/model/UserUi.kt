package ua.smartmir.picblend.features.remote_images.presentation.model

import androidx.compose.runtime.Stable
import ua.smartmir.picblend.features.remote_images.domain.model.UnsplashUser

@Stable
data class UserUi(
    val id: String,
    val username: String,
    val name: String,
    val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    val totalLikes: Int,
    val totalPhotos: Int,
    val totalCollections: Int,
    val profileImage: UrlsUi,
    val links: LinksUi
)

fun UnsplashUser.mapTo(): UserUi = UserUi(
    id = id,
    username = username,
    name = name,
    portfolioUrl = portfolioUrl,
    bio = bio,
    location = location,
    totalLikes = totalLikes,
    totalPhotos = totalPhotos,
    totalCollections = totalCollections,
    profileImage = profileImage.mapTo(),
    links = links.mapTo()
)