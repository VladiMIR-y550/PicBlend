package ua.smartmir.picblend.common.saveimage.data.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult.ErrorImageInfo
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult.SuccessImageInfo
import ua.smartmir.picblend.common.saveimage.domain.repository.ImageRepository
import java.io.IOException
import javax.inject.Inject

class GalleryStorage @Inject constructor(
    private val contentResolver: ContentResolver
) : ImageRepository {

    override suspend fun saveBitmap(
        image: Bitmap,
        fileName: String,
        onPhotoSaved: (SavedImageResult) -> Unit
    ) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.png")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PicBlend")
        }

        val imageUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return

        try {
            contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            onPhotoSaved(SuccessImageInfo(name = fileName, uri = imageUri))
        } catch (e: IOException) {
            onPhotoSaved(
                ErrorImageInfo(
                    error = e,
                    errorMessage = "CameraPhotoStorage: ${e.message}"
                )
            )
        }
    }
}