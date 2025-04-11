package ua.smartmir.picblend.core.base

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel <T : UiEffect> : ViewModel() {
    private val _effect = MutableSharedFlow<T>()
    val effect: SharedFlow<T> = _effect

    protected fun sendEffect(effect: T) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}

interface UiEffect

sealed interface HomeEffect : UiEffect {
    data class ShowToast(val message: String) : HomeEffect
    data class ShareImage(val uri: Uri) : HomeEffect
}

sealed interface CameraEffect : UiEffect {
    data class ShowToast(val message: String) : CameraEffect
}

sealed interface RemoteImagesEffect : UiEffect {
    data class ShowToast(val message: String) : RemoteImagesEffect
    data class Loading(val isLoading: Boolean) : RemoteImagesEffect
    data class CachedImage(val uri: Uri) : RemoteImagesEffect
}