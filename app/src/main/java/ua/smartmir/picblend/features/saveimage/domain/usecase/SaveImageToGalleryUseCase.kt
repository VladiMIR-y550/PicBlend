package ua.smartmir.picblend.features.saveimage.domain.usecase

import android.graphics.Bitmap
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult
import ua.smartmir.picblend.features.saveimage.domain.repository.ImageRepository
import ua.smartmir.picblend.di.Camera
import javax.inject.Inject

class SaveImageToGalleryUseCase @Inject constructor(
    @Camera private val imageRepository: ImageRepository
) {
    suspend fun saveImage(image: Bitmap, onPhotoTaken: (SavedImageResult) -> Unit) {
        imageRepository.saveBitmap(
            image,
            "IMG_${System.currentTimeMillis()}",
            onPhotoTaken
        )
    }
}