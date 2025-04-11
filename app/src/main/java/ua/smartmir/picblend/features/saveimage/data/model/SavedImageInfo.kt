package ua.smartmir.picblend.features.saveimage.data.model

import android.net.Uri

sealed class SavedImageResult {
    data class SuccessImageInfo(
        val name: String,
        val uri: Uri
    ) : SavedImageResult()

    data class ErrorImageInfo(
        val error: Exception? = null,
        val errorMessage: String = ""
    ): SavedImageResult()
}


