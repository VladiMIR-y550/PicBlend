package ua.smartmir.picblend.features.remote_images.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ua.smartmir.picblend.features.remote_images.domain.model.UnsplashLinks

data class UnsplashLinksDto(
    @Expose
    @SerializedName("self")
    val self: String,
    @Expose
    @SerializedName("html")
    val html: String,
    @Expose
    @SerializedName("photos")
    val photos: String?,
    @Expose
    @SerializedName("likes")
    val likes: String?,
    @Expose
    @SerializedName("portfolio")
    val portfolio: String?,
    @Expose
    @SerializedName("download")
    val download: String?,
    @Expose
    @SerializedName("download_location")
    val downloadLocation: String?
)

fun UnsplashLinksDto.mapTo(): UnsplashLinks = UnsplashLinks(
    self = self,
    html = html,
    photos = photos,
    likes = likes,
    portfolio = portfolio,
    download = download,
    downloadLocation = downloadLocation
)
