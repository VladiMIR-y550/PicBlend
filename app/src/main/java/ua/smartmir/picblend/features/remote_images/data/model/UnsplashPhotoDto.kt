package ua.smartmir.picblend.features.remote_images.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ua.smartmir.picblend.features.remote_images.domain.model.UnsplashPhoto

data class UnsplashPhotoDto(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("created_at")
    val createdAt: String,
    @Expose
    @SerializedName("width")
    val width: Int,
    @Expose
    @SerializedName("height")
    val height: Int,
    @Expose
    @SerializedName("color")
    val color: String? = "#000000",
    @Expose
    @SerializedName("likes")
    val likes: Int,
    @Expose
    @SerializedName("description")
    val description: String?,
    @Expose
    @SerializedName("urls")
    val urls: UnsplashUrlsDto,
    @Expose
    @SerializedName("links")
    val links: UnsplashLinksDto,
    @Expose
    @SerializedName("user")
    val user: UnsplashUserDto
)

fun UnsplashPhotoDto.mapTo(): UnsplashPhoto = UnsplashPhoto(
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

