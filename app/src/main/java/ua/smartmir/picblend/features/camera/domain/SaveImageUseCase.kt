package ua.smartmir.picblend.features.camera.domain

import android.graphics.Bitmap
import android.net.Uri
import ua.smartmir.picblend.features.camera.data.saveimage.ImageRepository
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend fun saveImageToGallery(image: Bitmap, onPhotoTaken: (Uri) -> Unit) {
        imageRepository.saveBitmapToGallery(image, onPhotoTaken)
    }
}