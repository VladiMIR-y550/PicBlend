package ua.smartmir.picblend.features.home.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.smartmir.picblend.R
import ua.smartmir.picblend.base.HomeEffect.ShareImage
import ua.smartmir.picblend.base.HomeEffect.ShowToast
import ua.smartmir.picblend.common.BarIconState
import ua.smartmir.picblend.common.CameraPermissionRequest
import ua.smartmir.picblend.common.FiltersRow
import ua.smartmir.picblend.common.filters.domain.model.FilterType
import ua.smartmir.picblend.core.hasRequiredCameraPermission
import ua.smartmir.picblend.core.toast
import ua.smartmir.picblend.features.camera.presentation.FilterStateEntity
import ua.smartmir.picblend.navigation.Navigator
import ua.smartmir.picblend.navigation.Screens.Companion.KEY_RETURNED_IMAGE

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navigator: Navigator,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onRemoteImagesClick: () -> Unit,
    onExitClick: () -> Unit,
    updateBarIconsState: (List<BarIconState>) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    navigator.getDataFromBackStackEntryFlow<Uri?>(
        KEY_RETURNED_IMAGE, defaultValue = null
    ) { imageUri ->
        viewModel.loadImageFromGallery(
            imageUri ?: return@getDataFromBackStackEntryFlow
        )
    }
    LaunchedEffect(Unit) {
        updateBarIconsState(
            listOf(
                BarIconState(
                    imageVector = Icons.Default.Save,
                    onClick = viewModel::saveImage
                ),
                BarIconState(
                    imageVector = Icons.Default.Share,
                    onClick = viewModel::shareImage
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ShareImage -> {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/png"
                        putExtra(Intent.EXTRA_STREAM, effect.uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(
                        Intent.createChooser(shareIntent, context.getString(R.string.share_image))
                    )
                }

                is ShowToast -> context.toast(effect.message)
            }
        }
    }


    if (uiState.isPermissionNeeded) {
        CameraPermissionRequest(
            onDismiss = viewModel::resetPermissionNeededState
        )
    }

    HomeUi(
        modifier = modifier,
        imageBitmap = uiState.image?.asImageBitmap(),
        filters = uiState.filterList,
        isPhotoFiltersShowing = uiState.isPhotoFiltersShowing,
        onImageFiltersClick = viewModel::showPhotoFilters,
        onFilterSelected = viewModel::changeFilter,
        onCameraClick = {
            if (context.hasRequiredCameraPermission()) {
                onCameraClick.invoke()
            } else {
                viewModel.launchRequestPermission()
            }
        },
        onGalleryClick = onGalleryClick,
        onNetworkClick = onRemoteImagesClick
    )
}

@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap?,
    filters: List<FilterStateEntity>,
    isPhotoFiltersShowing: Boolean,
    onImageFiltersClick: () -> Unit,
    onFilterSelected: (FilterType) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onNetworkClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            MainImageFrame(
                modifier = modifier
                    .weight(1F)
                    .fillMaxSize(),
                imageBitmap = imageBitmap,
                onImageFiltersClick = onImageFiltersClick,
            )

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1F),
                    onClick = onCameraClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.button_camera),
                        contentDescription = null
                    )
                }

                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1F),
                    onClick = onGalleryClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.button_gallery),
                        contentDescription = null
                    )
                }

                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1F),
                    onClick = onNetworkClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.button_internet),
                        contentDescription = null
                    )
                }
            }
        }
        if (isPhotoFiltersShowing) {
            FiltersRow(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
                filters = filters,
                onFilterSelected = onFilterSelected
            )
        }
    }
}

@Composable
fun MainImageFrame(
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap?,
    onImageFiltersClick: () -> Unit,
) {
    imageBitmap?.let {
        Box(
            modifier = modifier
                .wrapContentSize()
                .padding(16.dp)
                .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
        ) {
            Image(
                modifier = modifier,
                bitmap = imageBitmap,
                contentDescription = stringResource(R.string.uploaded_image),
                contentScale = ContentScale.Fit
            )
            IconButton(
                onClick = onImageFiltersClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoFilter,
                    contentDescription = stringResource(R.string.photo_filters)
                )
            }
        }
    } ?: Image(
        modifier = modifier
            .fillMaxWidth(),
        painter = painterResource(R.drawable.shape),
        contentDescription = stringResource(R.string.default_image)
    )
}


@Preview(showSystemUi = true)
@Composable
fun HomeUiPreview() {
    HomeUi(
        imageBitmap = null,
        filters = emptyList(),
        onCameraClick = {},
        onFilterSelected = {},
        onGalleryClick = {},
        onNetworkClick = {},
        onImageFiltersClick = {},
        isPhotoFiltersShowing = true
    )
}