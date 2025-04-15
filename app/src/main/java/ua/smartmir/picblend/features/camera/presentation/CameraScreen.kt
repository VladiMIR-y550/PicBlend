package ua.smartmir.picblend.features.camera.presentation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import ua.smartmir.picblend.R
import ua.smartmir.picblend.core.base.CameraEffect.ShowToast
import ua.smartmir.picblend.core.base.CollectEffects
import ua.smartmir.picblend.core.presentation.StableBitmap
import ua.smartmir.picblend.core.toast
import ua.smartmir.picblend.features.camera.presentation.model.CameraSettingsUi
import ua.smartmir.picblend.features.camera.presentation.model.FilterUiState
import ua.smartmir.picblend.features.filters.domain.model.FilterType
import ua.smartmir.picblend.features.filters.presentation.FiltersRow

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel,
    onImagePicked: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val controller = viewModel.launchCamera().controller

    CollectEffects(viewModel.effect, lifecycleOwner) { effect ->
        when (effect) {
            is ShowToast -> context.toast(effect.message)
        }
    }

    BackHandler {
        onImagePicked(uiState.lastImageUri)
    }

    CameraUi(
        modifier = modifier,
        image = uiState.image,
        lastImageUri = uiState.lastImageUri,
        availableCameras = uiState.cameras,
        filterList = uiState.filterList,
        isPhotoFiltersShowing = uiState.isPhotoFiltersShowing,
        cameraController = controller,
        switchFrontBackCamera = viewModel::switchFrontBackCamera,
        showPhotoFilters = viewModel::showPhotoFilters,
        takePhoto = viewModel::takePhoto,
        changeCamera = viewModel::changeCamera,
        changeFilter = viewModel::changeFilter
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraUi(
    modifier: Modifier = Modifier,
    image: StableBitmap?,
    lastImageUri: Uri?,
    availableCameras: List<CameraSettingsUi>,
    filterList: List<FilterUiState>,
    isPhotoFiltersShowing: Boolean,
    cameraController: LifecycleCameraController?,
    switchFrontBackCamera: () -> Unit,
    showPhotoFilters: () -> Unit,
    takePhoto: () -> Unit,
    changeCamera: (CameraSettingsUi) -> Unit,
    changeFilter: (FilterType) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CameraPreview(
            modifier = modifier.fillMaxSize(),
            controller = cameraController,
            processedBitmap = image
        )
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = switchFrontBackCamera,
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = stringResource(R.string.switch_camera)
                    )
                }

                IconButton(
                    onClick = showPhotoFilters,
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = stringResource(R.string.photo_filters)
                    )
                }
            }

            Column(
                modifier = modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isPhotoFiltersShowing) {
                    FiltersRow(
                        modifier = modifier,
                        filters = filterList,
                        onFilterSelected = changeFilter
                    )
                }

                CameraSelector(
                    modifier = modifier,
                    cameras = availableCameras,
                    onCameraSelected = changeCamera
                )

                IconButton(
                    modifier = modifier.size(64.dp),
                    onClick = takePhoto
                ) {
                    Icon(
                        modifier = modifier.size(40.dp),
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = stringResource(R.string.take_photo)
                    )
                }
            }
        }
        lastImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Last photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(16.dp)
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape)
            )
        }
    }
}

@Composable
fun CameraSelector(
    modifier: Modifier = Modifier,
    cameras: List<CameraSettingsUi>,
    onCameraSelected: (CameraSettingsUi) -> Unit
) {
    Surface(
        modifier = modifier
            .wrapContentSize()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.Gray
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.wrapContentSize()
        ) {
            items(cameras.size) { index ->
                val currentCamera = cameras[index]
                CameraToggle(
                    camera = currentCamera,
                    isSelected = currentCamera.isSelected,
                    onClick = { onCameraSelected(currentCamera) }
                )
            }
        }
    }

}

@Composable
fun CameraToggle(
    modifier: Modifier = Modifier,
    camera: CameraSettingsUi,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isSelected) Color.Green else Color.Black

    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Icon(
                modifier = Modifier.align(Alignment.TopCenter),
                imageVector = camera.focal.imageVector,
                contentDescription = camera.description,
                tint = backgroundColor
            )
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = camera.focal.focalValue?.toString()
                    ?: stringResource(R.string.dots),
                color = backgroundColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CameraUiPreview() {
    CameraUi(
        image = null,
        lastImageUri = null,
        availableCameras = listOf(
            CameraSettingsUi(
                isSelected = true,
                cameraId = "1",
                lensFacingLabel = R.string.camera,
                lensFacingId = 1,
                focal = Focal.Standard(2.2f)

            )
        ),
        filterList = listOf(
            FilterUiState(isSelected = true, filterType = FilterType.None),
            FilterUiState(filterType = FilterType.INVERT),
            FilterUiState(filterType = FilterType.GRAYSCALE),
            FilterUiState(filterType = FilterType.SEPIA)
        ),
        isPhotoFiltersShowing = true,
        cameraController = null,
        switchFrontBackCamera = {},
        showPhotoFilters = {},
        takePhoto = {},
        changeCamera = {},
        changeFilter = {}
    )
}

@Composable
@Preview(showBackground = true)
fun CameraSelectorPreview() {
    CameraSelector(
        cameras = listOf(
            CameraSettingsUi(
                isSelected = true,
                cameraId = "1",
                lensFacingLabel = R.string.camera,
                lensFacingId = 1,
                focal = Focal.Standard(2.2f)
            ),
            CameraSettingsUi(
                isSelected = false,
                cameraId = "1",
                lensFacingLabel = R.string.camera,
                lensFacingId = 1,
                focal = Focal.UltraWide(1.2f)
            )
        ),
        onCameraSelected = {}
    )
}