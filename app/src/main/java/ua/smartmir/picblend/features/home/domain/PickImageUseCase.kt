package ua.smartmir.picblend.features.home.domain

import android.graphics.Bitmap
import android.net.Uri
import ua.smartmir.picblend.features.home.data.GalleryRepository
import javax.inject.Inject

class PickImageUseCase @Inject constructor(
    private val galleryRepository: GalleryRepository
) {
    suspend fun imageByUri(uri: Uri): Bitmap? {
        return galleryRepository.loadBitmapFromUri(uri)
    }
}