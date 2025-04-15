package ua.smartmir.picblend.features.saveimage.domain.repository

import android.graphics.Bitmap
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult

interface ImageRepository {
    suspend fun saveBitmap(
        image: Bitmap,
        fileName: String,
        onPhotoSaved: (SavedImageResult) -> Unit
    )
}