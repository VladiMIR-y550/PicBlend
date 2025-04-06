package ua.smartmir.picblend.features.camera.presentation

import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import ua.smartmir.picblend.R
import ua.smartmir.picblend.features.camera.domain.FilterType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(modifier: Modifier = Modifier, viewModel: CameraViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val controller = viewModel.launchCamera().controller

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {

        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreview(
                modifier = modifier.fillMaxSize(),
                controller = controller,
                processedBitmap = uiState.mainPreviewBitmap
            )
            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = modifier.offset(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = stringResource(R.string.switch_camera)
                )
            }

            IconButton(
                onClick = viewModel::showPhotoFilters,
                modifier = modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoFilter,
                    contentDescription = stringResource(R.string.photo_filters)
                )
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = viewModel::takePhoto
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = stringResource(R.string.take_photo)
                    )
                }
            }
            uiState.lastImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Last photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .offset(16.dp, (-16.dp))
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                        .align(Alignment.BottomStart)
                )
            }

            if (uiState.isPhotoFiltersShowing) {
                FiltersRow(
                    modifier = modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp),
                    filters = uiState.filterList,
                    onFilterChosen = viewModel::changeFilter
                )
            }
        }
    }
}

@Composable
fun FiltersRow(
    modifier: Modifier = Modifier,
    filters: List<FilterStateEntity>,
    onFilterChosen: (FilterType) -> Unit,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(count = filters.size) { index ->
            val currentFilter = filters[index]

            Spacer(modifier.width(8.dp))
            Card(
                modifier = modifier
                    .size(width = 80.dp, height = 80.dp)
                    .border(
                        width = 1.dp,
                        color = if (currentFilter.isSelected) Color.Green else Color.Unspecified,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                onClick = { onFilterChosen(currentFilter.filterType) }
            ) {
                Column {
                    currentFilter.filteredBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentScale = ContentScale.Crop,
                            contentDescription = currentFilter.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(text = currentFilter.name)
                }
            }
        }
    }
}