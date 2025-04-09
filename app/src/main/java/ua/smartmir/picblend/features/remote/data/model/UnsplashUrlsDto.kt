package ua.smartmir.picblend.features.remote.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ua.smartmir.picblend.features.remote.domain.model.UnsplashUrls

data class UnsplashUrlsDto(
    @Expose
    @SerializedName("thumb")
    val thumb: String?,
    @Expose
    @SerializedName("small")
    val small: String,
    @Expose
    @SerializedName("medium")
    val medium: String?,
    @Expose
    @SerializedName("regular")
    val regular: String?,
    @Expose
    @SerializedName("large")
    val large: String?,
    @Expose
    @SerializedName("full")
    val full: String?,
    @Expose
    @SerializedName("raw")
    val raw: String?
)

fun UnsplashUrlsDto.mapTo(): UnsplashUrls = UnsplashUrls(
    thumb = thumb ?: "",
    small = small,
    medium = medium ?: "",
    regular = regular ?: "",
    large = large ?: "",
    full = full ?: "",
    raw = raw ?: ""
)