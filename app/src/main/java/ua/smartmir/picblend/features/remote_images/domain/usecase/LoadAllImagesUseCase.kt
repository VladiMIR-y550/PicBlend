package ua.smartmir.picblend.features.remote_images.domain.usecase

import ua.smartmir.picblend.features.remote_images.data.repository.RemoteImagesRepository
import ua.smartmir.picblend.features.remote_images.domain.model.UnsplashPhoto
import javax.inject.Inject

class LoadAllImagesUseCase @Inject constructor(
    private val remoteImagesRepository: RemoteImagesRepository
) {
    private var currentPage = 1

    suspend fun loadImages(): Result<List<UnsplashPhoto>> {
        return remoteImagesRepository.images(currentPage).fold(
            onSuccess = { photos ->
                currentPage++
                Result.success(photos)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    suspend fun trackDownload(url: String) = remoteImagesRepository.trackDownload(url)
}