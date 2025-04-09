package ua.smartmir.picblend.features.remote.data.repository

import ua.smartmir.picblend.features.remote.data.model.mapTo
import ua.smartmir.picblend.features.remote.domain.model.UnsplashPhoto
import javax.inject.Inject

interface RemoteImagesRepository {

    suspend fun images(page: Int): Result<List<UnsplashPhoto>>
    suspend fun trackDownload(url: String)

    class UnsplashImagesRepository @Inject constructor(
        private val unsplashDataSource: ImagesDataSource,
    ) : RemoteImagesRepository {

        override suspend fun images(page: Int): Result<List<UnsplashPhoto>> {
            return unsplashDataSource.getImages(page).fold(
                onSuccess = { dtoPhotos ->
                    Result.success(dtoPhotos.map { it.mapTo() })
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }

        override suspend fun trackDownload(url: String) {
            unsplashDataSource.safeTrackDownload(url)
        }
    }
}