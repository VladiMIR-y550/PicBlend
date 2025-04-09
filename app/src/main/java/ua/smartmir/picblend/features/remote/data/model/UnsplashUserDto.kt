package ua.smartmir.picblend.features.remote.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ua.smartmir.picblend.features.remote.domain.model.UnsplashUser

data class UnsplashUserDto(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("username")
    val username: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("portfolio_url")
    val portfolioUrl: String?,
    @Expose
    @SerializedName("bio")
    val bio: String?,
    @Expose
    @SerializedName("location")
    val location: String?,
    @Expose
    @SerializedName("total_likes")
    val totalLikes: Int,
    @Expose
    @SerializedName("total_photos")
    val totalPhotos: Int,
    @Expose
    @SerializedName("total_collections")
    val totalCollections: Int,
    @Expose
    @SerializedName("profile_image")
    val profileImage: UnsplashUrlsDto,
    @Expose
    @SerializedName("links")
    val links: UnsplashLinksDto
)

fun UnsplashUserDto.mapTo(): UnsplashUser = UnsplashUser(
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