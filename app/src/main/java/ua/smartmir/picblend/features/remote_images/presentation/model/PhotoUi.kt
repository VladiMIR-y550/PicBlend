package ua.smartmir.picblend.features.remote_images.presentation.model

import androidx.compose.runtime.Stable
import ua.smartmir.picblend.features.remote_images.domain.model.UnsplashPhoto

@Stable
data class PhotoUi(
    val id: String,
    val createdAt: String,
    val width: Int,
    val height: Int,
    val ratio: Float,
    val color: String?,
    val likes: Int,
    val description: String?,
    val urls: UrlsUi,
    val links: LinksUi,
    val user: UserUi
)

fun UnsplashPhoto.mapTo(): PhotoUi {
    val safeRatio = if (height != 0) width.toFloat() / height else 1f
    return PhotoUi(
        id = id,
        createdAt = createdAt,
        width = width,
        height = height,
        ratio = safeRatio,
        color = color,
        likes = likes,
        description = description,
        urls = urls.mapTo(),
        links = links.mapTo(),
        user = user.mapTo()
    )
}

