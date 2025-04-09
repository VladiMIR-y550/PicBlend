package ua.smartmir.picblend.features.remote.domain.usecase

import ua.smartmir.picblend.features.remote.data.repository.RemoteImagesRepository
import ua.smartmir.picblend.features.remote.domain.model.UnsplashPhoto
import javax.inject.Inject

class LoadAllImagesUseCase @Inject constructor(
    private val remoteImagesRepository: RemoteImagesRepository
) {
    suspend fun loadImages(page: Int): Result<List<UnsplashPhoto>> {
        return remoteImagesRepository.images(page)
    }

    suspend fun trackDownload(url: String) = remoteImagesRepository.trackDownload(url)
}