package ua.smartmir.picblend.features.home.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.smartmir.picblend.R
import ua.smartmir.picblend.common.BarIconState
import ua.smartmir.picblend.common.CameraPermissionRequest
import ua.smartmir.picblend.core.hasRequiredCameraPermission
import ua.smartmir.picblend.navigation.Navigator
import ua.smartmir.picblend.navigation.Screens.Companion.KEY_RETURNED_IMAGE

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navigator: Navigator,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
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
    LaunchedEffect(null) {
        updateBarIconsState(
            listOf(
                BarIconState(
                    imageVectorId = R.drawable.share_line,
                    onClick = {}
                )
            )
        )
    }
    if (uiState.isPermissionNeeded) {
        CameraPermissionRequest(
            onDismiss = viewModel::resetPermissionNeededState
        )
    }

    HomeUi(
        modifier = modifier,
        imageBitmap = uiState.bitmap,
        onCameraClick = {
            if (context.hasRequiredCameraPermission()) {
                onCameraClick.invoke()
            } else {
                viewModel.launchRequestPermission()
            }
        },
        onGalleryClick = onGalleryClick,
        onNetworkClick = {}//todo
    )
}

@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    imageBitmap: Bitmap?,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onNetworkClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        imageBitmap?.let {
            Image(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1F),
                bitmap = imageBitmap.asImageBitmap(),
                contentDescription = null
            )
        } ?: Image(
            modifier = modifier
                .fillMaxWidth()
                .weight(1F),
            painter = painterResource(R.drawable.shape),
            contentDescription = null
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
}


@Preview(showSystemUi = true)
@Composable
fun HomeUiPreview() {
    HomeUi(
        imageBitmap = null,
        onCameraClick = {},
        onGalleryClick = {},
        onNetworkClick = {}
    )
}