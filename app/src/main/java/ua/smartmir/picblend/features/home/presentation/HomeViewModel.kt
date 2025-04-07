package ua.smartmir.picblend.features.home.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.smartmir.picblend.common.filters.domain.model.FilterType
import ua.smartmir.picblend.common.filters.domain.usecase.ApplyFilterUseCase
import ua.smartmir.picblend.common.filters.domain.usecase.ChooseFilterUseCase
import ua.smartmir.picblend.di.EditorFilters
import ua.smartmir.picblend.features.camera.domain.SaveImageUseCase
import ua.smartmir.picblend.features.camera.presentation.mapToStateEntity
import ua.smartmir.picblend.features.home.domain.PickImageUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @EditorFilters applyFilterUseCase: ApplyFilterUseCase,
    @EditorFilters private val filtersUseCase: ChooseFilterUseCase,
    private val pickImageUseCase: PickImageUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {

    private val image = MutableStateFlow<Bitmap?>(null)
    private val isPermissionNeeded = MutableStateFlow<Boolean>(false)
    private val isFiltersShowed = MutableStateFlow<Boolean>(false)

    val uiState = combine(
        image.filterNotNull(),
        filtersUseCase.generateFilterPreviews(image),
        isFiltersShowed,
        isPermissionNeeded,
    ) { image, filters, isFiltersShowed, isPermissionNeeded ->
        HomeState(
            image = applyFilterUseCase.applySelectedFilter(image)?.asImageBitmap(),
            filterList = filters.map { it.mapToStateEntity() },
            isPermissionNeeded = isPermissionNeeded,
            isPhotoFiltersShowing = isFiltersShowed
        )
    }.flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeState())

    fun launchRequestPermission() {
        isPermissionNeeded.update { true }
    }

    fun resetPermissionNeededState() {
        isPermissionNeeded.update { false }
    }

    fun loadImageFromGallery(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            pickImageUseCase.imageByUri(uri)?.let { selectedBitmap ->
                image.update { selectedBitmap }
            }
        }
    }

    fun showPhotoFilters() {
        isFiltersShowed.update { !it }
    }

    fun changeFilter(filterType: FilterType) {
        filtersUseCase.updateChosenFilter(filterType)
    }
}