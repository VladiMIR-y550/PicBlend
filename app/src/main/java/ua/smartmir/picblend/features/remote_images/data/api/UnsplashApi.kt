package ua.smartmir.picblend.features.remote_images.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import ua.smartmir.picblend.features.remote_images.data.model.UnsplashPhotoDto

interface UnsplashApi {

    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): List<UnsplashPhotoDto>

    @GET
    suspend fun trackDownload(@Url downloadLocation: String): Response<Unit>
}