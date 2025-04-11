package ua.smartmir.picblend.features.remote_images.presentation.model

import ua.smartmir.picblend.features.remote_images.domain.model.UnsplashPhoto

data class PhotoUi(
    val id: String,
    val createdAt: String,
    val width: Int,
    val height: Int,
    val color: String?,
    val likes: Int,
    val description: String?,
    val urls: UrlsUi,
    val links: LinksUi,
    val user: UserUi
)

fun UnsplashPhoto.mapTo(): PhotoUi = PhotoUi(
    id = id,
    createdAt = createdAt,
    width = width,
    height = height,
    color = color,
    likes = likes,
    description = description,
    urls = urls.mapTo(),
    links = links.mapTo(),
    user = user.mapTo()
)

