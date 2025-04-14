package ua.smartmir.picblend.features.home.presentation

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.smartmir.picblend.R
import ua.smartmir.picblend.core.CameraPermissionRequest
import ua.smartmir.picblend.core.base.CollectEffects
import ua.smartmir.picblend.core.base.HomeEffect.ShareImage
import ua.smartmir.picblend.core.base.HomeEffect.ShowToast
import ua.smartmir.picblend.core.hasRequiredCameraPermission
import ua.smartmir.picblend.core.presentation.main.BarIconState
import ua.smartmir.picblend.core.presentation.navigation.Navigator
import ua.smartmir.picblend.core.presentation.navigation.Screens.Companion.KEY_RETURNED_IMAGE
import ua.smartmir.picblend.core.toast
import ua.smartmir.picblend.features.camera.presentation.model.FilterUiState
import ua.smartmir.picblend.features.filters.domain.model.FilterType
import ua.smartmir.picblend.features.filters.presentation.FiltersRow

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
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    navigator.getDataFromBackStackEntryFlow<Uri?>(
        KEY_RETURNED_IMAGE, defaultValue = null
    ) { imageUri ->
        viewModel.loadImageFromGallery(
            imageUri ?: return@getDataFromBackStackEntryFlow
        )
    }

    BackHandler(onBack = viewModel::showExitAppDialog)

    CollectEffects(viewModel.effect, lifecycleOwner) { effect ->
        when (effect) {
            is ShareImage -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, effect.uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(
                    Intent.createChooser(
                        shareIntent,
                        context.getString(R.string.share_image)
                    )
                )
            }

            is ShowToast -> context.toast(effect.message)
        }
    }

    uiState.image?.let {
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

    if (uiState.isPermissionNeeded) {
        CameraPermissionRequest(
            onDismiss = viewModel::updateIsPermissionNeeded
        )
    }

    if (uiState.isExitDialogShowed) {
        ExitAppDialog(
            onExitAction = {
                viewModel.showExitAppDialog(false)
                onExitClick()
            },
            onDismiss = {
                viewModel.showExitAppDialog(false)
            }
        )
    }

    HomeUi(
        modifier = modifier,
        imageBitmap = uiState.image?.bitmap?.asImageBitmap(),
        filters = uiState.filterList,
        isPhotoFiltersShowing = uiState.isPhotoFiltersShowing,
        onImageFiltersClick = viewModel::showPhotoFilters,
        onFilterSelected = viewModel::changeFilter,
        onCameraClick = {
            if (context.hasRequiredCameraPermission()) {
                onCameraClick.invoke()
            } else {
                viewModel.updateIsPermissionNeeded(true)
            }
        },
        onGalleryClick = onGalleryClick,
        onNetworkClick = onRemoteImagesClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExitAppDialog(
    modifier: Modifier = Modifier,
    onExitAction: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        content = {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(id = R.string.dialog_title_exit),
                    )
                    Text(
                        modifier = modifier.padding(vertical = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        text = stringResource(R.string.dialog_text_maybe_not_time_yet)
                    )
                    TextButton(
                        modifier = modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        onClick = onExitAction,
                        content = {
                            Text(text = stringResource(R.string.dialog_button_exit))
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap?,
    filters: List<FilterUiState>,
    isPhotoFiltersShowing: Boolean,
    onImageFiltersClick: () -> Unit,
    onFilterSelected: (FilterType) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onNetworkClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = modifier,
                    onClick = onCameraClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.button_camera),
                        contentDescription = null
                    )
                }

                Button(
                    modifier = modifier,
                    onClick = onGalleryClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.button_gallery),
                        contentDescription = null
                    )
                }

                Button(
                    modifier = modifier,
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
        val aspectRatio = it.width.toFloat() / it.height.toFloat()

        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .padding(16.dp)
                .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = stringResource(R.string.uploaded_image),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onImageFiltersClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.AutoFixHigh,
                    contentDescription = stringResource(R.string.photo_filters),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    } ?: Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            painter = painterResource(R.drawable.pic_blend),
            contentDescription = stringResource(R.string.default_image),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.add_photo_and_throw_on_some_filters),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
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