package ua.smartmir.picblend.features.home.presentation

import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.smartmir.picblend.base.BaseViewModel
import ua.smartmir.picblend.features.home.domain.PickImageUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pickImageUseCase: PickImageUseCase
) : BaseViewModel<HomeState>(HomeState()) {


    fun launchRequestPermission() {
        _uiState.update {
            it.copy(isPermissionNeeded = true)
        }
    }

    fun resetPermissionNeededState() {
        _uiState.update {
            it.copy(isPermissionNeeded = false)
        }
    }

    fun loadImageFromGallery(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            pickImageUseCase.imageByUri(uri)?.let { selectedBitmap ->
                _uiState.update { state ->
                    state.copy(bitmap = selectedBitmap)
                }
            }
        }
    }
}